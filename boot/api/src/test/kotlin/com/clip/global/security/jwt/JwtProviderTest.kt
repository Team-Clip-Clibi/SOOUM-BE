package com.clip.global.security.jwt

import com.clip.data.member.entity.Role
import com.clip.global.IntegrationTestSupport
import com.clip.global.security.jwt.JwtProvider.Companion.ID_CLAIM
import com.clip.global.security.jwt.JwtProvider.Companion.ISSUER
import com.clip.global.security.jwt.JwtProvider.Companion.REFRESH_SUBJECT
import com.clip.global.security.jwt.JwtProvider.Companion.ROLE_CLAIM
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Date

class JwtProviderTest: IntegrationTestSupport() {

    @Autowired
    lateinit var provider: JwtProvider
    @Autowired
    lateinit var jwtProperties: JwtProperties

    @DisplayName("만료된 토큰 유효기간 추출 테스트")
    @Test
    fun getTokenExpirationTest() {
        // given
        val id = 1L
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val expiration = now.minusDays(JwtProperties.REFRESH_TOKEN_EXPIRE_MONTH).truncatedTo(ChronoUnit.SECONDS) //지난 날짜로 만료일 설정
        val expiredToken = Jwts.builder()
            .setIssuedAt(Date.from(now.toInstant()))
            .setIssuer(ISSUER)
            .setExpiration(Date.from(expiration.toInstant()))
            .setSubject(REFRESH_SUBJECT)
            .claim(ID_CLAIM, id)
            .claim(ROLE_CLAIM, Role.USER)
            .signWith(
                Keys.hmacShaKeyFor(jwtProperties.key.toByteArray(StandardCharsets.UTF_8)),
                SignatureAlgorithm.HS256
            )
            .compact()

        // when
        val tokenExpiration = provider.getTokenExpiration(expiredToken)

        // then
        Assertions.assertThat(tokenExpiration).isEqualTo(expiration.toLocalDateTime())
    }
}