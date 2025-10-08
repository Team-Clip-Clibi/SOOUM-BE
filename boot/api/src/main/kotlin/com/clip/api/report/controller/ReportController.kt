package com.clip.api.report.controller

import com.clip.api.docs.report.ReportDocs
import com.clip.api.report.service.ReportFacade
import com.clip.global.security.annotation.AccessUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val reportFacade: ReportFacade,
): ReportDocs {
    @PostMapping("/cards/{cardId}")
    override fun createCardReport(
        @PathVariable cardId: Long,
        @RequestBody reportDto: ReportDto,
        @AccessUser userId: Long
    ): ResponseEntity<Void> =
        reportFacade.createCardReport(cardId, reportDto.reportType, userId)
            .let { ResponseEntity.ok().build() }
}