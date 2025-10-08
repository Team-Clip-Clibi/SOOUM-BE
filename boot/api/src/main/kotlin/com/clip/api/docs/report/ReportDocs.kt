package com.clip.api.docs.report

import com.clip.api.report.controller.ReportDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Report", description = "신고 API")
interface ReportDocs {

    @Operation(summary = "카드 신고 API", description = """
        - id에 해당하는 카드를 신고하는 API
    """)
    fun createCardReport(cardId: Long, reportDto: ReportDto, userId: Long): ResponseEntity<Void>
}