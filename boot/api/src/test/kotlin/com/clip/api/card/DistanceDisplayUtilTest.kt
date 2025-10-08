package com.clip.api.card

import com.clip.api.card.util.DistanceDisplayUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

class DistanceDisplayUtilTest {

    @Test
    fun distanceCalculateTest() {
        // given
        val cardLocation = DistanceDisplayUtil.calculate(
            cardLocation = null,
            latitude = 37.0,
            longitude = 127.0
        )
        // when & then
        Assertions.assertThat(cardLocation).isNull()

        // given
        val cardLocation2 = DistanceDisplayUtil.calculate(
            cardLocation = null,
            latitude = null,
            longitude = 127.0
        )
        // when & then
        Assertions.assertThat(cardLocation2).isNull()

        // given
        val cardLocation3 = DistanceDisplayUtil.calculate(
            cardLocation = null,
            latitude = 37.0,
            longitude = null
        )
        // when & then
        Assertions.assertThat(cardLocation3).isNull()

        // given
        val cardLocation4 = DistanceDisplayUtil.calculate(
            cardLocation = null,
            latitude = null,
            longitude = null
        )
        // when & then
        Assertions.assertThat(cardLocation4).isNull()

        //37.254212, 127.213412 집근처 마트
        //37.498221, 127.027793 강남
        //37.499211, 127.027254
        val geometry = GeometryFactory()
        val coordinate = Coordinate(37.254212, 127.213412)
        val targetPoint = geometry.createPoint(coordinate)

        // given
        val cardLocation5 = DistanceDisplayUtil.calculateAndFormat(
            cardLocation = targetPoint,
            127.027793,
            37.498221
        )
        println("cardLocation5 = ${cardLocation5}")
    }

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