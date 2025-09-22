package com.clip.api.img.controller.dto

enum class DefaultImgCategory(
    val description: String,
) {
    READ_ONLY("하위호완을 위한 읽기 전용 이미지"),
    COLOR("컬러"),
    MEMO("메모지"),
    NATURE("자연"),
    DAILY("일상"),
    FOOD("음식"),
    MOOD("무드")
}