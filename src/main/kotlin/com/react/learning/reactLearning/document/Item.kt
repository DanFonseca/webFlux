package com.react.learning.reactLearning.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Item(
    @Id
    val id: String?,
    val description: String,
    var price: Double
) {
}