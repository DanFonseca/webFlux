package com.react.learning.reactLearning.controller

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.test.StepVerifier

@WebFluxTest (FluxAndMonoController::class)
@RunWith(SpringRunner::class)
@DirtiesContext
class FluxAndMonoControllerTest {

    @Autowired
    lateinit var webClientTest: WebTestClient

    @Test
    fun webFluxApproach1() {

        val result = webClientTest.get()
            .uri("/flux")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .returnResult(Int::class.java)
            .responseBody

        StepVerifier.create(result)
            .expectSubscription()
            .expectNext(1, 2, 3, 4, 5)
    }


    @Test
    fun webFluxApproach2() {

         webClientTest.get()
            .uri("/flux")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Int::class.java)
            .hasSize(5)
    }

    @Test
    fun webFluxApproach3() {
        val expectedIntegersResult = arrayListOf(1, 2, 3, 4, 5)

        val result = webClientTest.get()
            .uri("/flux")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Int::class.java)
            .returnResult()

        assertEquals(expectedIntegersResult, result.responseBody)
    }

    @Test
    fun webFluxApproach4() {
        val expectedIntegersResult = arrayListOf(1, 2, 3, 4, 5)

        webClientTest.get()
            .uri("/flux")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Int::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Int>>
            { response ->
                assertEquals(expectedIntegersResult, response.responseBody)
            }
    }

    @Test
    fun webFluxApproach5() {
        val expectedIntegersResult = arrayListOf(1)

        webClientTest.get()
            .uri("/monoStream")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Int::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Int>>
            { response ->
                assertEquals(expectedIntegersResult, response.responseBody)
            }
    }


}