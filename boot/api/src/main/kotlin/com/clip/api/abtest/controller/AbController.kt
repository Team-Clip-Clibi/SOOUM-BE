package com.clip.api.abtest.controller

import com.clip.api.abtest.controller.dto.ArticleCardAbTestResponse
import com.clip.api.abtest.service.AbUseCase
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cards")
class AbController(
    private val abUseCase: AbUseCase
) {

    @GetMapping("/article")
    fun getLatestArticleCard(
        @AccessUser userId: Long
    ): ResponseEntity<ArticleCardAbTestResponse> =
        abUseCase.getLatestArticleCard(userId)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

}

