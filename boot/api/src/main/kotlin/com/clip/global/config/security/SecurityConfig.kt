package com.clip.global.config.security

import com.clip.global.security.jwt.JwtAuthenticationFilter
import com.clip.global.security.jwt.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProvider: JwtProvider,
    private val excludeAuthPathProperties: ExcludeAuthPathProperties
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }

        http.authorizeHttpRequests { request ->
            excludeAuthPathProperties.paths.forEach { authPath ->
                request.requestMatchers(HttpMethod.valueOf(authPath.method), authPath.pathPattern).permitAll()
            }
        }

        http.authorizeHttpRequests { request ->
            request
                .requestMatchers(HttpMethod.POST, "/cards").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/cards/{cardPk}").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/users/token").hasRole("USER") // 토큰 재발급
                .anyRequest().authenticated()
        }

        http.sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        http.exceptionHandling { except ->
            except.authenticationEntryPoint { request, response, authException ->
                response.sendError(response.status, "토큰 오류")
            }
        }

        return http.build()
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtProvider, excludeAuthPathProperties)
    }

}
