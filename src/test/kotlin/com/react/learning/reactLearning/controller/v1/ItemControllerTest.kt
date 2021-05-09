package com.react.learning.reactLearning.controller.v1

import com.react.learning.reactLearning.common.ItemBuilder.itemsBuilder
import com.react.learning.reactLearning.constant.ItemConstant.ITEM_V1_END_POINT
import com.react.learning.reactLearning.document.Item
import com.react.learning.reactLearning.repository.ItemReactiveRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
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
import reactor.core.publisher.Mono

@SpringBootTest()
@RunWith(SpringRunner::class)
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles(profiles = ["dev"])
class ItemControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var itemReactiveRepository: ItemReactiveRepository

    @Before
    fun setUp() {
        itemReactiveRepository.deleteAll()
            .block()
        itemReactiveRepository.saveAll(itemsBuilder())
            .subscribe {
                println(it)
            }
    }

    @Test
    fun getAllItems() {
        webTestClient.get()
            .uri(ITEM_V1_END_POINT)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .hasSize(5)
    }

    @Test
    fun `getA All Items Id Must not null`() {
        webTestClient.get()
            .uri("/v1/items")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Item::class.java)
            .hasSize(5)
            .consumeWith<WebTestClient.ListBodySpec<Item>> { it ->
                it.responseBody?.forEach {
                    assertTrue(it.id != null)
                }
            }
    }

    @Test
    fun `Get Item By Id with Path Variable`() {
        webTestClient.get()
            .uri("$ITEM_V1_END_POINT/{id}", "1")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.price", 299.99)
    }

    @Test
    fun `Not Found By Id with Path Variable`() {
        webTestClient.get()
            .uri("$ITEM_V1_END_POINT/{id}", "SMILE")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `Must Create An Item`() {
        val item = Item(
            null,
            "Macbook Pro",
            1299.0
        )

        webTestClient
            .post()
            .uri(ITEM_V1_END_POINT)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .body(Mono.just(item), Item::class.java)
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").isNotEmpty
            .jsonPath("$.description").isEqualTo("Macbook Pro")
            .jsonPath("$.price").isEqualTo(1299.0)

    }

    @Test
    fun `Delete An Item`() {
        webTestClient.delete()
            .uri("$ITEM_V1_END_POINT/{id}", "APPLE#1")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent
            .expectBody(Void::class.java)
    }


    @Test
    fun `Update An Item`() {
        val newItem = Item(description = "updated", price = 0.0)

        webTestClient.put()
            .uri("$ITEM_V1_END_POINT/{id}", 1)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(Mono.just(newItem), Item::class.java)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.description").isEqualTo("updated")
    }


}