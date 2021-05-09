package com.react.learning.reactLearning.handler

import com.mongodb.internal.connection.Server
import com.react.learning.reactLearning.constant.ItemConstant
import com.react.learning.reactLearning.constant.ItemConstant.ITEM_V1_FUNCTIONAL_END_POINT
import com.react.learning.reactLearning.document.Item
import com.react.learning.reactLearning.repository.ItemReactiveRepository
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.lang.RuntimeException
import java.net.URI

@Component
class ItemHandler(
    val itemReactiveRepository: ItemReactiveRepository
) {

    fun getAllItems(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .body(itemReactiveRepository.findAll(), Item::class.java)
    }

    fun getById(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id")
        val found = itemReactiveRepository.findById(id) ?: throw RuntimeException("Item Not Found")

        return found.flatMap { item ->
            ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(Mono.just(item), Item::class.java)
        }.switchIfEmpty(ServerResponse.notFound().build())

    }

    fun createAnItem(serverRequest: ServerRequest): Mono<ServerResponse> {
        return serverRequest.bodyToMono(Item::class.java)
            .flatMap {
                ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(itemReactiveRepository.save(it), Item::class.java)
            }.switchIfEmpty(ServerResponse.badRequest().build())
    }

    fun deleteById(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id = serverRequest.pathVariable("id")
        itemReactiveRepository.deleteById(id)

        return ServerResponse
            .noContent()
            .build()
    }

    fun updateItem(serverRequest: ServerRequest): Mono<ServerResponse> {

        return serverRequest.bodyToMono(Item::class.java).flatMap { body ->
            itemReactiveRepository.findById(serverRequest.pathVariable("id"))
                .flatMap { found ->
                    itemReactiveRepository.save(
                        Item(
                            found.id,
                            body.description ?: found.description,
                            body.price ?: found.price
                        )
                    )
                }
        }.flatMap {
            ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(Mono.just(it), Item::class.java)
        }.switchIfEmpty(ServerResponse.notFound().build())

    }
}
