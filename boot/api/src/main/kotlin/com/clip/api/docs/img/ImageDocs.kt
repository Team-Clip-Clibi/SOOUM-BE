package com.clip.api.docs.img

import com.clip.api.img.controller.dto.DefaultImagesResponse
import com.clip.api.img.controller.dto.ImageUrlInfoResponse
import com.clip.api.img.controller.dto.VerificationResultResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "Image", description = "Image API")
interface ImageDocs {

    @Operation(summary = "프로필 이미지 업로드 API", description = """
        - 사용자가 프로필 이미지를 업로드할 수 있도록 합니다.
        - 이미지는 S3에 저장되며, 저장된 이미지의 URL을 반환합니다.
        - 이미지 파일은 JPEG 형식만 허용됩니다.
        - Rekognition을 사용하여 부적절한 이미지인지 검사를 수행합니다.
    """)
    fun uploadProfileImage(): ResponseEntity<ImageUrlInfoResponse>

    @Operation(summary = "기본 배경 리스트 조회 API", description = """
        - 카드 작성 시 사용되는 배경 사진 중 기본 배경 사진의 이름과 현재 사용 가능 여부를 조회합니다.
    """)
    fun getDefaultImgs(): ResponseEntity<DefaultImagesResponse>


    @Operation(summary = "사용자 지정 배경 이미지 업로드 url 발급 API", description = """
        - 사용자가 지정한 배경 이미지를 업로드할 수 있도록 업로드 URL을 발급합니다.
    """)
    fun getUploadCardImgUrl(): ResponseEntity<ImageUrlInfoResponse>

    @Operation(summary = "배경 이미지 사용 가능 여부 조회 API", description = """
        - 업로드된 카드 배경 이미지의 부적절 여부를 검사하고, 그 결과를 반환합니다.
        - isAvailableImg 값이 true이면 해당 이미지를 카드 배경으로 사용할 수 있습니다.
        - S3에서 해당 이미지를 찾을 수 없는 경우 404 NOT FOUND 응답을 반환합니다.
    """)
    fun getVerificationImagResult(imgName: String): ResponseEntity<VerificationResultResponse>
}