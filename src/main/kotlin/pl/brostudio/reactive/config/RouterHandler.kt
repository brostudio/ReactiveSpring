package pl.brostudio.reactive.config

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors.toMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import pl.brostudio.reactive.entities.TemperatureSensor
import pl.brostudio.reactive.repositories.IoTRepository
import reactor.core.publisher.Mono
import java.util.logging.Logger

@Component
class RouterHandler(val iotRepository: IoTRepository<TemperatureSensor>) {

    companion object {
        val logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun getAll(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .body(iotRepository.findAll())
    }

    fun getId(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id: String = serverRequest.pathVariable("id")
        return ServerResponse.ok()
                .body(iotRepository.findById(id))
    }

    fun addDevice(serverRequest: ServerRequest): Mono<ServerResponse> {
        val iotDevice = serverRequest.body(toMono(TemperatureSensor::class.java)).cache()

        return iotDevice
                .flatMap { iotRepository.findById(it.id) }
                .flatMap {
                    iotDevice.flatMap {
                        val errorMsg = String.format("Device exists with ID: %s", it.id)
                        logger.info(errorMsg)
                        ServerResponse.badRequest().syncBody(errorMsg)
                    }
                }
                .switchIfEmpty(
                        iotDevice
                                .flatMap {
                                    logger.info(String.format("New device was added with ID: %s", it.id))
                                    iotRepository.save(it)
                                }
                                .flatMap { ServerResponse.ok().build() }
                )
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