package com.react.learning.reactLearning.handler

import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@RunWith(SpringRunner::class)
@AutoConfigureWebTestClient
@DirtiesContext
class SampleHandlerFunctionTest {

    @Autowired
    lateinit var webClientTest: WebTestClient

    @Test
    fun approach1 () {

        webClientTest.get()
            .uri("/function/flux")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Int::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Int>> { r ->
                assertEquals(arrayListOf(1,2,3,4), r.responseBody)
            }
    }

    @Test
    fun approach2 () {
        val respone = webClientTest.get()
            .uri("/function/mono")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody(Int::class.java)
            .returnResult()

        assertEquals(1, respone.responseBody)
    }
}