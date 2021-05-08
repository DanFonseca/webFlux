package com.react.learning.reactLearning.repository

import com.react.learning.reactLearning.document.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import java.util.*


interface ItemReactiveRepository : ReactiveMongoRepository<Item, String>{

    fun findByDescription(description: String) : Flux<Item>
}