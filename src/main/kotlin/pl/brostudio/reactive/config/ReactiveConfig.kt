package pl.brostudio.reactive.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router
import pl.brostudio.reactive.repositories.IoTRepository

@Configuration
class ReactiveConfig {

    @Bean
    fun routerFunction(routerHandler: RouterHandler) =
        router {
            "/iot".nest {
                GET("/all", routerHandler::getAll)
                "/{id}".nest {
                    GET("/", routerHandler::getId)
                    //GET("/events", routerHandler::getEvents)
                }
                PUT("/addDevice", routerHandler::addDevice)
            }
        }

}