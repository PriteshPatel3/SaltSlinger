package com.example.myapplication.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

// Item Data Class
data class Item(
    val id: Int,
    val name: String,
    val code: String,
    val resourceType: String, // e.g., "spirit", "monk", "treasure"
    val baseIncrement: Int = 1, // Default increment
    val triggerType: String, // what causes this Item to generate its resource
    val type: String, // generator or buff
    val synergyRules: (Map<String, Boolean>) -> Int = { 0 }, // Additional increment based on other items
)

// GameResources Data Class
data class GameResources(
    val spirit: MutableState<Int> = mutableStateOf(0),
    val prowess: MutableState<Int> = mutableStateOf(0),
    val treasure: MutableState<Int> = mutableStateOf(0),
    val storm: MutableState<Int> = mutableStateOf(0),
    val monk: MutableState<Int> = mutableStateOf(0),
    val extraResource: MutableState<Int> = mutableStateOf(0),
    val battlemage: MutableState<Int> = mutableStateOf(0),
    val jAscendency: MutableState<Int> = mutableStateOf(0),
    val birgiRed: MutableState<Int> = mutableStateOf(0),
//    val isPanelExpanded: Boolean = false
    var isPanelExpanded: MutableState<Boolean> = mutableStateOf(false),
    var sectionGenerator: MutableState<Boolean> = mutableStateOf(false),
    var sectionBuff: MutableState<Boolean> = mutableStateOf(false)
//    var isPanelExpanded: MutableState<Boolean> = mutableStateOf(false)
) {
    val resourceMap: Map<String, MutableState<Int>> = mapOf(
        "spirit" to spirit,
        "prowess" to prowess,
        "treasure" to treasure,
        "storm" to storm,
        "monk" to monk,
        "extraResource" to extraResource,
        "battlemage buff" to battlemage,
        "Jeskai Asc buff" to jAscendency,
        "Birgi Red" to birgiRed,
    )
}
