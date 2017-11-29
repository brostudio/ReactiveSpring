package pl.brostudio.reactive.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class ReactiveConfig {

    @Bean
    fun routerFunction(routerHandler: RouterHandler) =
            router {
                "/rest/employee".nest {
                    GET("/all", routerHandler::getAll)
                    "/{id}".nest {
                        GET("/", routerHandler::getId)
                        GET("/events", routerHandler::getEvents)
                    }
                }

            }

}