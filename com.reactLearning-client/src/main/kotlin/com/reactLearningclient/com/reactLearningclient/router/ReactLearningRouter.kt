package com.reactLearningclient.com.reactLearningclient.router

import com.reactLearningclient.com.reactLearningclient.constant.ItemConstant.ITEM_V1_FUNCTIONAL_END_POINT
import com.reactLearningclient.com.reactLearningclient.handler.ReactLearningHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ReactLearningRouter {

    @Bean
    fun getAllItems(reactLearningHandler: ReactLearningHandler) = coRouter {
        accept(APPLICATION_JSON).nest {
            GET(ITEM_V1_FUNCTIONAL_END_POINT, reactLearningHandler::getItemsUsingRetrieve)
            GET("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", reactLearningHandler::getItemById)
            POST("$ITEM_V1_FUNCTIONAL_END_POINT", reactLearningHandler::postItem)
            PUT("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", reactLearningHandler::putItem)
         //   DELETE("$ITEM_V1_FUNCTIONAL_END_POINT/{id}", reactLearningHandler::deleteItem)
        }
    }

}