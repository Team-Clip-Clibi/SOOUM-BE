package com.clip.global.security.jwt

import com.clip.data.member.entity.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.security.Key

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties
) {
    companion object {
        const val ISSUER = "sooum"
        const val ACCESS_SUBJECT = "AccessToken"
        const val REFRESH_SUBJECT = "RefreshToken"
        const val ID_CLAIM = "id"
        const val ROLE_CLAIM = "role"
    }

    fun createAccessToken(id: Long, role: Role): String {
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val expiration = now.plusDays(JwtProperties.ACCESS_TOKEN_EXPIRE_DAY)
        val claims = mapOf(
            ID_CLAIM to id,
            ROLE_CLAIM to role
        )
        return Jwts.builder()
            .setIssuedAt(Date.from(now.toInstant()))
            .setIssuer(ISSUER)
            .setExpiration(Date.from(expiration.toInstant()))
            .setSubject(ACCESS_SUBJECT)
            .setClaims(claims)
            .signWith(
                getSigningKey(),
                SignatureAlgorithm.HS256
            )
            .compact()
    }

    fun createRefreshToken(id: Long, role: Role): String {
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val expiration = now.plusMonths(JwtProperties.REFRESH_TOKEN_EXPIRE_MONTH)
        val claims = mapOf(
            ID_CLAIM to id,
            ROLE_CLAIM to role
        )
        return Jwts.builder()
            .setIssuedAt(Date.from(now.toInstant()))
            .setIssuer(ISSUER)
            .setExpiration(Date.from(expiration.toInstant()))
            .setSubject(REFRESH_SUBJECT)
            .setClaims(claims)
            .signWith(
                getSigningKey(),
                SignatureAlgorithm.HS256
            )
            .compact()
    }

    fun validateToken(token: String): Boolean =
        runCatching {
            val claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body

            val expiration = claims.expiration
            expiration.after(Date())
        }.getOrDefault(false)

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val role = getRole(token) ?: Role.USER
        val authorities = setOf(SimpleGrantedAuthority(role.field))

        return UsernamePasswordAuthenticationToken(
            User(claims[ID_CLAIM, Long::class.java].toString(), "", authorities),
            token,
            authorities
        )
    }

    fun getId(token: String?): Long? {
        return token?.let {
            getClaims(it)[ID_CLAIM, Long::class.java]
        }
    }

    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body

    private fun getRole(token: String): Role? {
        val claims = getClaims(token)
        return claims[ROLE_CLAIM, Role::class.java]
    }

    private fun getSigningKey(): Key =
        Keys.hmacShaKeyFor(jwtProperties.key.toByteArray(StandardCharsets.UTF_8))

}