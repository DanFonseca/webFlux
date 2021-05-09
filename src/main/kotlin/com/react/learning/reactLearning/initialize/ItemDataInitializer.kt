package com.react.learning.reactLearning.initialize

import com.react.learning.reactLearning.document.Item
import com.react.learning.reactLearning.repository.ItemReactiveRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ItemDataInitializer() : CommandLineRunner {

    @Autowired
    lateinit var itemReactiveRepository: ItemReactiveRepository

    override fun run(vararg args: String?) {
        initializeSetup()
    }

    fun itemsBuilder(): List<Item> {
        return listOf(
            Item(null, "Apple Watch", 149.99),
            Item(null, "iPhone 11", 649.99),
            Item("1", "Apple TV", 299.99),
            Item("APPLE#1", "iPod", 150.00)
        )
    }

    fun initializeSetup() {
        itemReactiveRepository.run {
            this.deleteAll()
                .block()

            this.saveAll(itemsBuilder())
                .subscribe {
                    println(it)
                }
        }
    }
}