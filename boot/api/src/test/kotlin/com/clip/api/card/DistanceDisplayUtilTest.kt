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
            1.0 to "100m",
            50.0 to "100m",
            99.0 to "100m",
            100.0 to "100m",
            101.0 to "100m",
            150.0 to "100m",
            199.0 to "100m",
            499.0 to "400m",
            500.0 to "500m",
            501.0 to "500m",
            599.0 to "500m",
            999.0 to "900m",
            1000.0 to "1km",
            1100.0 to "1.1km",
            1999.0 to "1.9km",
            4999.0 to "4.9km",
            5000.0 to "5km",
            5500.0 to "5.5km",
            9999.0 to "9.9km"
        )

        // when & then
        testCases.forEach { (input, expected) ->
            Assertions.assertThat(DistanceDisplayUtil.format(input)).isEqualTo(expected)
        }
    }
}