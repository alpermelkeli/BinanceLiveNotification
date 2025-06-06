package com.alpermelkeli.livenotification.notifications.navigation.enums

import android.graphics.Color


/*To and from shows start and stop progress between 0 to 100*/
data class TrafficState(
    val density: TrafficDensity,
    val from: Int,
    val to: Int
)

enum class TrafficDensity(val color: Int){
    LOW(color = Color.parseColor("#4CAF50")), // GREEN
    MEDIUM(color = Color.parseColor("#F7C23A")), // YELLOW
    HIGH(color = Color.parseColor("#F44336")), // RED
    BLOCKED(color = Color.parseColor("#616161")) // DARK GRAY
}