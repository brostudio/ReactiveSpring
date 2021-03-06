package pl.brostudio.reactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.server.router
import pl.brostudio.reactive.config.RouterHandler

@SpringBootApplication
class ReactiveApplication {

    @Bean
    fun routerFunction(routerHandler: RouterHandler) =
            router {
                "/iot".nest {
                    GET("/all", routerHandler::getAll)
                    "/{id}".nest {
                        GET("/", routerHandler::getId)
                        GET("/events", routerHandler::getEventById)
                    }
                    POST("/device", routerHandler::addDevice)
                    POST("/event", routerHandler::event)
                    DELETE("/remove/{id}", routerHandler::remove)
                }
            }

/*
    @Bean
    fun init(iotRepository: IoTRepository<TemperatureSensor>) = CommandLineRunner {
        iotRepository.deleteAll()
                .subscribe(null, null, {
                    Stream.of(
                            TemperatureSensor(UUID.randomUUID().toString(), 21.3F),
                            TemperatureSensor(UUID.randomUUID().toString(), 24.5F),
                            TemperatureSensor(UUID.randomUUID().toString(), 22.1F)
                    ).forEach { sensorData ->
                        run {
                            iotRepository
                                    .save(sensorData)
                                    .subscribe(System.out::println)
                        }
                    }
                })
    }
*/

}

fun main(args: Array<String>) {
    runApplication<ReactiveApplication>(*args)
}
