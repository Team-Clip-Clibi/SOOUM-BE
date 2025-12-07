package com.clip.api.docs.img

import com.clip.api.img.controller.dto.DefaultImagesResponse
import com.clip.api.img.controller.dto.ImageUrlInfoResponse
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
        - 카드 작성 시 사용되는 배경 사진 중 기본 배경 사진의 카테고리와, 각 카테고리에 해당하는 사진 이름 및 url 정보를 가진 DTO 리스트를 조회합니다.
        - 기본 배경 사진은 COLOR, MEMO, NATURE, SENSITIVITY, FOOD, ABSTRACT, EVENT 총 7가지 카테고리로 구분됩니다.
        - 카테고리별 이미지는 7장씩 반환됩니다.
        - EVENT 카테고리의 이미지 리스트는 이벤트 배경 사진이 오픈된 경우에만 반환되며, 이벤트 기간이 아닌경우 null을 반환합니다.
    """)
    fun getDefaultImgs(): ResponseEntity<DefaultImagesResponse>


    @Operation(summary = "사용자 지정 배경 이미지 업로드 url 발급 API", description = """
        - 사용자가 지정한 배경 이미지를 업로드할 수 있도록 업로드 URL을 발급합니다.
    """)
    fun getUploadCardImgUrl(): ResponseEntity<ImageUrlInfoResponse>

}