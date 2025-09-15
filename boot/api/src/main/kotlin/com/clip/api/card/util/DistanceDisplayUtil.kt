package com.clip.api.card.util

/***
 * 피드에서 보여지는 거리 포맷으로 변경하는 유틸 클래스
 * @param distanceMeter meter단위의 Int 거리를 입력받음
 * @return String 포맷된 거리 (예: 100m, 1.1km etc.)
 */
class DistanceDisplayUtil {
    companion object {
        fun format(distanceMeter: Int): String {
            return when {
                distanceMeter <= 100 -> "100m"
                distanceMeter < 1000 -> {
                    val rounded = (distanceMeter / 100) * 100
                    "${rounded}m"
                }
                else -> {
                    val rounded = (distanceMeter / 100) * 100
                    val km = rounded / 1000.0
                    if (km % 1.0 == 0.0) {
                        "${km.toInt()}km"
                    } else {
                        "%.1fkm".format(km)
                    }
                }
            }
        }
    }
}