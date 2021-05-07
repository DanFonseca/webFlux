package com.react.learning.reactLearning.handler

import org.apache.commons.logging.Log
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class SampleHandlerFunction {

    fun flux(serverRequest: ServerRequest): Mono<ServerResponse> {
        println(serverRequest.localAddress())

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                Flux.just(1, 2, 3, 4),
                Int::class.java
            )
    }

    fun mono(serverRequest: ServerRequest): Mono<ServerResponse> {

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                Mono.just(1),
                Int::class.java
            )

    }


}