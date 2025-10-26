package com.clip.global.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(
            io.swagger.v3.oas.models.Components().addSecuritySchemes(
                "Bearer Token",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .`in`(SecurityScheme.In.HEADER)
                    .name("Authorization")
            )
        )
        .info(
            Info()
                .title("SOOUM API")
                .version("1.0")
                .description("""
                    - Token 검증 애러: FORBIDDEN(403) 
                    - Token 만료 : UNAUTHORIZED(401) 
                    - 이미지 없음: NOT_FOUND(404)
                    - 부적절한 이미지: UNPROCESSABLE_ENTITY(422)
                    - 파라미터 누락: BAD_REQUEST(400)
                    - 카드 작성 차단 유저의 카드 작성 요청: BAD_REQUEST(400)
                    - 삭제된 카드에 대한 리소스 생성 및 수정(좋아요, 좋아요 취소, 답카드 작성, 신고): GONE(410)
                    - 이미 신고된 카드의 중복 신고 요청: CONFLICT(409)
                """.trimIndent())
        )
        .addServersItem(Server().url("/"))
        .addSecurityItem(
            SecurityRequirement().addList("Bearer Token")
        )
}
