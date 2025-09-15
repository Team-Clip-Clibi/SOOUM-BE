package com.clip.api.card

import com.clip.api.card.util.DistanceDisplayUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DistanceDisplayUtilTest {

    @DisplayName("거리 변환 유틸 테스트")
    @Test
    fun distanceDisplayTest() {
        // given
        val testCases = mapOf(
            1 to "100m",
            50 to "100m",
            99 to "100m",
            100 to "100m",
            101 to "100m",
            150 to "100m",
            199 to "100m",
            499 to "400m",
            500 to "500m",
            501 to "500m",
            599 to "500m",
            999 to "900m",
            1000 to "1km",
            1100 to "1.1km",
            1999 to "1.9km",
            4999 to "4.9km",
            5000 to "5km",
            5500 to "5.5km",
            9999 to "9.9km"
        )

        // when & then
        testCases.forEach { (input, expected) ->
            Assertions.assertThat(DistanceDisplayUtil.format(input)).isEqualTo(expected)
        }
    }
}