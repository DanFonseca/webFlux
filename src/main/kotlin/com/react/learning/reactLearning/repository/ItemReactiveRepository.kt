package com.react.learning.reactLearning.repository

import com.react.learning.reactLearning.document.Item
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface ItemReactiveRepository : ReactiveMongoRepository<Item, String>{

    fun findByDescription(description: String) : Mono<Item>
    fun deleteByDescription(description: String) : Mono<Void>
}