package com.example.myapplication
import com.example.myapplication.models.GameResources
import com.example.myapplication.models.Item

fun getItemsByTriggerType(items: List<Item>, triggerType: String): List<Item> {
    return items.filter { it.triggerType == triggerType }
}

fun calculateIncrement(resourceType: String, checkedStates: Map<String, Boolean>, item: Item): Int {
    var totalIncrement = 0
    if (item.resourceType == resourceType) {
        totalIncrement += item.baseIncrement + item.synergyRules(checkedStates)
    }
    return totalIncrement
}

fun cast(
    triggerType: String,
    gameResources: GameResources,
    items: List<Item>,
    checkedStates: Map<String, Boolean>
) {
    val filtheredItems = getItemsByTriggerType(items , triggerType)
        .filter { item: Item -> checkedStates[item.code] == true }
    filtheredItems.forEach { item: Item ->
        val resourceIncrement = calculateIncrement(item.resourceType, checkedStates, item)
        gameResources.resourceMap[item.resourceType]?.let { resourceState ->
            resourceState.value = resourceState.value + resourceIncrement
        }
    }
}