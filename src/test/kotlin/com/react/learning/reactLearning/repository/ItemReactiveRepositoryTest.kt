package com.react.learning.reactLearning.repository

import com.react.learning.reactLearning.common.ItemBuilder
import com.react.learning.reactLearning.document.Item
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@DataMongoTest
@RunWith(SpringRunner::class)
@ActiveProfiles(profiles = ["dev"])
@DirtiesContext
class ItemReactiveRepositoryTest {

    @Autowired
    lateinit var itemReactiveRepository: ItemReactiveRepository


    @BeforeEach
    fun setUp() {
        itemReactiveRepository.deleteAll()
            .thenMany(Flux.fromIterable(ItemBuilder.itemsBuilder()))
            .flatMap { item -> itemReactiveRepository.save(item) }
            .doOnNext { item -> println("Item: $item - Saved") }
            .blockLast()
    }

    @Test
    fun getAllItems() {
        StepVerifier.create(
            itemReactiveRepository.findAll()
        )
            .expectSubscription()
            .expectNextCount(4)
            .verifyComplete()
    }

    @Test
    fun getId() {
        StepVerifier.create(
            itemReactiveRepository.findById("APPLE#1")
        )
            .expectSubscription()
            .expectNextMatches {
                it.description == ItemBuilder.itemsBuilder()[3].description
            }
            .verifyComplete()
    }


    @Test
    fun findByDescription() {
        StepVerifier.create(
            itemReactiveRepository.findByDescription("Apple Watch")
        )
            .expectSubscription()
            .expectNextMatches {
                it.description == ItemBuilder.itemsBuilder()[0].description
            }
            .verifyComplete()
    }

    @Test
    fun insertAnItem() {

        val item = Item(
            id = null,
            description = "Echo Dot - Amazon",
            price = 180.99
        )

        itemReactiveRepository.save(item)
            .run {
                StepVerifier.create(this)
                    .expectSubscription()
                    .expectNextMatches { saved ->
                        saved.id != null &&
                                saved.description == "Echo Dot - Amazon"
                    }
                    .verifyComplete()
            }
    }


    @Test
    fun updateAnItem() {
        val newPrice = 89.0

        itemReactiveRepository.findByDescription("iPod")
            .map {
                it.price = newPrice
                return@map it
            }.flatMap {
                itemReactiveRepository.save(it)
            }.also { it ->
                StepVerifier.create(it)
                    .expectSubscription()
                    .expectNextMatches {
                        it.price == newPrice
                    }
            }
    }

    @Test
    fun `Must Delete By Id` () {
        val deletedItem = itemReactiveRepository
                .deleteById("APPLE#1").log()

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete()

        StepVerifier
                .create(itemReactiveRepository.findById("APPLE#1"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete()
    }

    @Test
    fun `Must Delete By Description` () {
        val deletedItem = itemReactiveRepository
                .deleteByDescription("iPhone 11").log()

        StepVerifier.create(deletedItem)
                .expectSubscription()
                .verifyComplete()

        StepVerifier
                .create(itemReactiveRepository.findByDescription("iPhone 11"))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete()
    }
}