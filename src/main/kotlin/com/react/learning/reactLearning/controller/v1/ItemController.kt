package com.react.learning.reactLearning.controller.v1

import com.react.learning.reactLearning.constant.ItemConstant.ITEM_V1_END_POINT
import com.react.learning.reactLearning.document.Item
import com.react.learning.reactLearning.repository.ItemReactiveRepository
import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(ITEM_V1_END_POINT)
class ItemController(
    val itemReactiveRepository: ItemReactiveRepository
) {

    @GetMapping
    fun getAllItems() =
        itemReactiveRepository.findAll()

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postAnItem(@RequestBody item: Item) =
        itemReactiveRepository
            .save(item)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteItem(@PathVariable id: String) =
        itemReactiveRepository
            .deleteById(id)


    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): Mono<ResponseEntity<Item>> =
        itemReactiveRepository
            .findById(id)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }.defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))


    @PutMapping("/{id}")
    fun updateAnItem(
        @PathVariable id: String,
        @RequestBody item: Item
    ): Mono<ResponseEntity<Item>> {
        return itemReactiveRepository
            .findById(id)
            .flatMap {
                itemReactiveRepository.save(
                    Item(
                        it.id,
                        item.description,
                        item.price
                    )
                )
            }.map {
                ResponseEntity(it, HttpStatus.OK)
            }.defaultIfEmpty(ResponseEntity(HttpStatus.NOT_FOUND))
    }
}