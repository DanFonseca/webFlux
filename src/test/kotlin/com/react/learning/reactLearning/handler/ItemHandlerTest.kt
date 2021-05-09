package com.react.learning.reactLearning.handler

import com.react.learning.reactLearning.common.ItemBuilder.itemsBuilder
import com.react.learning.reactLearning.constant.ItemConstant.ITEM_V1_FUNCTIONAL_END_POINT
import com.react.learning.reactLearning.document.Item
import com.react.learning.reactLearning.repository.ItemReactiveRepository
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier


@SpringBootTest
@RunWith(SpringRunner::class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("dev")
class ItemHandlerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var itemReactiveRepository: ItemReactiveRepository

    @Before
    fun seTup() {
        itemReactiveRepository.deleteAll()
            .block()
        itemReactiveRepository.saveAll(itemsBuilder())
            .blockLast()
    }

    @Test
    fun `Get All Items`() {
        webTestClient.get()
            .uri(ITEM_V1_FUNCTIONAL_END_POINT)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .hasSize(5)
    }

    @Test
    fun `Get All Item with not null ids`() {
        webTestClient.get()
            .uri(ITEM_V1_FUNCTIONAL_END_POINT)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Item>> {
                it.responseBody?.forEach { item ->
                    assert(item.id != null)
                }
            }
    }

    @Test
    fun `Get All Item with Flux`() {
        val flux = webTestClient.get()
            .uri(ITEM_V1_FUNCTIONAL_END_POINT)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .returnResult()
            .responseBody

        StepVerifier.create(Flux.just(flux))
            .expectSubscription()
            .consumeNextWith {
                assert(it.size == 4)
            }
            .verifyComplete()

    }

    @Test
    fun `Get An Item By Id`() {
        webTestClient.get()
            .uri("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", 1)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id")
            .isEqualTo(1)
    }

    @Test
    fun `Get An Item By Id Not Found`() {
        webTestClient.get()
            .uri("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", 12)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `Post An Item`() {
        val item = Item(
            null,
            "Create",
            10.0
        )

        webTestClient.post()
            .uri(ITEM_V1_FUNCTIONAL_END_POINT)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(Mono.just(item), Item::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isNotEmpty
    }

    @Test
    fun `Delete By Id`() {
        webTestClient.delete()
            .uri("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", 1)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `Must Not Update`() {
        seTup()

        val newItem = Item(
            null,
            null,
            null
        )

        webTestClient.put()
            .uri("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", 1)
            .accept(APPLICATION_JSON)
            .body(Mono.just(newItem), Item::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.description")
            .isEqualTo("Apple TV")
    }

    @Test
    fun `Update An Item By Id`() {
        val newItem = Item(
            null,
            "Updated",
            null
        )

        webTestClient.put()
            .uri("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", 1)
            .accept(APPLICATION_JSON)
            .body(Mono.just(newItem), Item::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.description")
            .isEqualTo("Updated")
    }


    @Test
    fun `Must Return Not Found`() {
        val newItem = Item(
            null,
            null,
            null
        )

        webTestClient.put()
            .uri("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", 1231)
            .accept(APPLICATION_JSON)
            .body(Mono.just(newItem), Item::class.java)
            .exchange()
            .expectStatus().isNotFound
    }
}