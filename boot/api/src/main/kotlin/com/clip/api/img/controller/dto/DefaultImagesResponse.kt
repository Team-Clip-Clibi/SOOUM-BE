package com.clip.api.img.controller.dto

data class DefaultImagesResponse(
    val defaultImages: Map<DefaultImgCategory, List<ImgInfo>>
)

data class ImgInfo(
    val imgName: String,
    val url: String,
)