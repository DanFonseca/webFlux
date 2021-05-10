package com.reactLearningclient.com.reactLearningclient.handler

import com.reactLearningclient.com.reactLearningclient.constant.ItemConstant.LOCALHOST
import com.reactLearningclient.com.reactLearningclient.model.Item
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class ReactLearningHandler(
) {
    var webClient = WebClient.create(LOCALHOST)

    suspend fun getItemsUsingRetrieve(serverRequest: ServerRequest): ServerResponse {

        val items = webClient
            .get()
            .uri("/v1/fun/items")
            .accept(APPLICATION_JSON)
            .retrieve()
            .awaitBody<List<Item>>()

        return ServerResponse
            .status(HttpStatus.OK)
            .bodyValueAndAwait(items)
    }

    suspend fun getItemById(serverRequest: ServerRequest): ServerResponse {
        val id = serverRequest.pathVariable("id")

        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .bodyValueAndAwait(
                webClient.get()
                    .uri("/v1/fun/items/$id")
                    .retrieve()
                    .awaitBody<Item>()
            )
    }

    suspend fun postItem(request: ServerRequest): ServerResponse {
        val item = request.awaitBody<Item>()

        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .bodyValueAndAwait(
                webClient.post()
                    .uri("/v1/fun/items/")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(item)
                    .retrieve()
                    .awaitBody<Item>()
            )
    }

    suspend fun putItem(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id")
        val itemToBeUpdate = request.awaitBody<Item>()

        return ServerResponse.ok()
            .contentType(APPLICATION_JSON)
            .bodyValueAndAwait(
                webClient.put()
                    .uri("/v1/fun/items/$id")
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .bodyValue(itemToBeUpdate)
                    .retrieve()
                    .awaitBody<Item>()
            )
    }

//    suspend fun deleteItem(request: ServerRequest): ServerResponse {
//        val id = request.pathVariable("id")
//
//        return ServerResponse.ok().bodyValueAndAwait(
//            webClient.delete()
//                .uri("/v1/fun/items/$id")
//                .retrieve()
//                .bodyToMono(Item::class.java)
//        )


   // }

}