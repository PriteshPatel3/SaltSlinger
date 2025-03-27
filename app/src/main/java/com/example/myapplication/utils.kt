package com.example.myapplication

fun getItemsByTriggerType(items: List<Item>, triggerType: String): List<Item> {
    return items.filter { it.triggerType == triggerType }
}