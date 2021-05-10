package com.react.learning.reactLearning.route

import com.react.learning.reactLearning.constant.ItemConstant.ITEM_V1_FUNCTIONAL_END_POINT
import com.react.learning.reactLearning.handler.ItemHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class ItemRouter {

    @Bean
    fun getAllItems(itemHandler: ItemHandler) = router {
        accept(MediaType.APPLICATION_JSON)
            .nest {
                GET(ITEM_V1_FUNCTIONAL_END_POINT, itemHandler::getAllItems)
                GET("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", itemHandler::getById)
                POST("$ITEM_V1_FUNCTIONAL_END_POINT", itemHandler::createAnItem)
                DELETE("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", itemHandler::deleteById)
                PUT("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", itemHandler::updateItem)
            }
    }

}