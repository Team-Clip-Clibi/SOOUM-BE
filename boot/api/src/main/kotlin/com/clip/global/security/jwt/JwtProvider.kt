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
import java.time.LocalDateTime

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

    fun createAccessToken(id: Long?, role: Role): String {
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val expiration = now.plusDays(JwtProperties.ACCESS_TOKEN_EXPIRE_DAY)
        return Jwts.builder()
            .setIssuedAt(Date.from(now.toInstant()))
            .setIssuer(ISSUER)
            .setExpiration(Date.from(expiration.toInstant()))
            .setSubject(ACCESS_SUBJECT)
            .claim(ID_CLAIM, id)
            .claim(ROLE_CLAIM, role)
            .signWith(
                getSigningKey(),
                SignatureAlgorithm.HS256
            )
            .compact()
    }

    fun createRefreshToken(id: Long?, role: Role): String {
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val expiration = now.plusMonths(JwtProperties.REFRESH_TOKEN_EXPIRE_MONTH)
        return Jwts.builder()
            .setIssuedAt(Date.from(now.toInstant()))
            .setIssuer(ISSUER)
            .setExpiration(Date.from(expiration.toInstant()))
            .setSubject(REFRESH_SUBJECT)
            .claim(ID_CLAIM, id)
            .claim(ROLE_CLAIM, role)
            .signWith(
                getSigningKey(),
                SignatureAlgorithm.HS256
            )
            .compact()
    }

    fun isTokenOwner(token: String, userId: Long): Boolean =
        getClaims(token)
            .get(ID_CLAIM,Long::class.java)
            .equals(userId)


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
            User(claims[ID_CLAIM].toString(), "", authorities),
            token,
            authorities
        )
    }
    private fun getClaims(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body

    private fun getRole(token: String): Role? {
        val claims = getClaims(token)
        return Role.getRole(claims[ROLE_CLAIM]?.toString())
    }

    private fun getSigningKey(): Key =
        Keys.hmacShaKeyFor(jwtProperties.key.toByteArray(StandardCharsets.UTF_8))

    fun reissueToken(refreshToken: String, userId: Long): TokenDto =
        refreshToken
            .also { require(isRefreshToken(it)) { "Not Refresh Token" } }
            .also { require(isTokenOwner(it, userId)) { "Token Owner Mismatch" } }
            .also { require(validateToken(it)) { "Invalid Refresh Token" } }
            .let {
                val newAccessToken = reissueAccessToken(it, userId)
                val newRefreshToken = rotationRefreshToken(it, userId)
                TokenDto(
                    accessToken = newAccessToken,
                    refreshToken = newRefreshToken
                )
            }

    private fun reissueAccessToken(refreshToken: String, userId: Long): String =
        refreshToken
            .let { getRole(it) ?: Role.USER }
            .let { createAccessToken(userId, it) }

    private fun rotationRefreshToken(refreshToken: String, userId: Long): String =
        refreshToken
            .let { refreshToken -> getRole(refreshToken) ?: Role.USER }
            .let { role ->
                Jwts.builder()
                    .setIssuedAt(
                        Date.from(
                            ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toInstant()
                        )
                    )
                    .setIssuer(ISSUER)
                    .setExpiration(
                        Date.from(
                            getTokenExpiration(refreshToken)
                                .atZone(ZoneId.of("Asia/Seoul"))
                                .toInstant()
                        )
                    )
                    .setSubject(REFRESH_SUBJECT)
                    .claim(ID_CLAIM, userId)
                    .claim(ROLE_CLAIM, role)
                    .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                    )
                    .compact()
            }

    fun getTokenExpiration(token: String): LocalDateTime =
        getClaims(token).expiration.toInstant()
            .atZone(ZoneId.of("Asia/Seoul"))
            .toLocalDateTime()

    private fun isRefreshToken(refreshToken: String): Boolean =
        getClaims(refreshToken).subject.equals(REFRESH_SUBJECT)
}


data class TokenDto(
    val accessToken: String,
    val refreshToken: String
)