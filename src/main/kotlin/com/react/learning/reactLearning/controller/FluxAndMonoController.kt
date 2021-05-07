package com.react.learning.reactLearning.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.awt.PageAttributes
import java.time.Duration

@RestController
class FluxAndMonoController {

    @GetMapping("/flux")
    fun fluxCount () : Flux<Int> {
        return Flux.just(1,2,3,4,5)
            .log()
    }

    @GetMapping("/fluxsteam", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun fluxCountStram () : Flux<Int> {
        return Flux.just(1,2,3,4,5)
            .delayElements(Duration.ofSeconds(1))
            .log()
    }

    @GetMapping("/monoStream")
    fun monoStream () : Mono<Int>{
        return Mono.just(1)
            .log()
    }
}