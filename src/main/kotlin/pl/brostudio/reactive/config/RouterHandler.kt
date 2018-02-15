package pl.brostudio.reactive.config

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import pl.brostudio.reactive.entities.IoT
import pl.brostudio.reactive.entities.IoTEvent
import pl.brostudio.reactive.repositories.IoTRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*
import java.util.stream.Stream

@Component
class RouterHandler(val iotRepository: IoTRepository) {
    fun getAll(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .body(iotRepository.findAll(), IoT::class.java)
    }

    fun getId(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id: String = serverRequest.pathVariable("id")
        return ServerResponse.ok()
                .body(iotRepository.findById(id), IoT::class.java)
    }

/*
    fun getEvents(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id: String = serverRequest.pathVariable("id")
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        iotRepository.findById(id)
                                .flatMapMany { iot ->
                                    val interval = Flux.interval(Duration.ofSeconds(2))

                                    val employeeEventFlux = Flux.fromStream(
                                            Stream.generate({
                                                IoTEvent(iot, Date())
                                            })
                                    )

                                    Flux.zip(interval, employeeEventFlux)
                                            .map { o -> o.t2 }
                                }
                        , IoTEvent::class.java)
    }
*/
}