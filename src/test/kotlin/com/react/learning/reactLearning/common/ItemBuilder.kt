package com.react.learning.reactLearning.common

import com.react.learning.reactLearning.document.Item

object ItemBuilder {
    fun itemsBuilder(): List<Item> {
        return listOf(
            Item(null, "Apple Watch", 149.99),
            Item(null, "iPhone 11", 649.99),
            Item("1", "Apple TV", 299.99),
            Item("APPLE#1", "iPod", 150.00)
        )
    }
}