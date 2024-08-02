package com.example.openshock.data

import kotlinx.serialization.Serializable

enum class ControlType {
    Stop,
    Shock,
    Vibrate,
    Sound
}

@Serializable
data class ControlData(
    val id: String,
    val duration: Int,
    val intensity: Int,
    val type: ControlType
)