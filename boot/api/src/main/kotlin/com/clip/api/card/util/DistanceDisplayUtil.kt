package com.clip.api.card.util

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point

/***
 * 피드에서 보여지는 거리 포맷으로 변경하는 유틸 클래스
 * @param distanceMeter meter단위의 Int 거리를 입력받음
 * @return String 포맷된 거리 (예: 100m, 1.1km etc.)
 */
class DistanceDisplayUtil {
    companion object {

        fun calculateAndFormat(
            cardLocation: Point?,
            latitude: Double?,
            longitude: Double?
        ): String? {
            val distanceMeter = calculate(cardLocation, latitude, longitude) ?: return null
            return format(distanceMeter)
        }

        fun calculate(
            cardLocation: Point?,
            latitude: Double?,
            longitude: Double?
        ): Double? {
            if (isInvalidLocationInfo(cardLocation, latitude, longitude)) {
                return null
            }

            val geometry = GeometryFactory()
            val coordinate = Coordinate(longitude!!, latitude!!)
            val targetPoint = geometry.createPoint(coordinate)

            return cardLocation!!.distance(targetPoint) * 100
        }


        fun format(distanceKm: Double): String {
            return if (distanceKm < 1.0) {
                // 1km 미만 → 미터 단위로 표시
                val meter = (distanceKm * 1000).toInt()
                "${meter}m"
            } else {
                // 1km 이상 → 0.1km 단위로 표시
                val roundedKm = (distanceKm * 10).toInt() / 10.0
                if (roundedKm % 1.0 == 0.0) {
                    "${roundedKm.toInt()}km"
                } else {
                    "%.1fkm".format(roundedKm)
                }
            }
        }

        private fun isInvalidLocationInfo(
            cardLocation: Point?,
            latitude: Double?,
            longitude: Double?
        ): Boolean {
            return cardLocation == null || latitude == null || longitude == null
        }
    }
}