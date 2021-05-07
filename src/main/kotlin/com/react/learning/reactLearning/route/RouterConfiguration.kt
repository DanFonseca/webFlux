package com.react.learning.reactLearning.route

import com.react.learning.reactLearning.handler.SampleHandlerFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class RouterConfiguration {

    @Bean
    fun myRoute(sampleHandlerFunction: SampleHandlerFunction) = router {
        accept(MediaType.APPLICATION_JSON).nest {
            GET("/function/flux", sampleHandlerFunction::flux)
            GET("/function/mono", sampleHandlerFunction::mono)
        }
    }
}
