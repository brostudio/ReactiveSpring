package pl.brostudio.reactive.config

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors.toMono
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import pl.brostudio.reactive.entities.IoTEvent
import pl.brostudio.reactive.entities.TemperatureSensor
import pl.brostudio.reactive.repositories.IoTRepository
import pl.brostudio.reactive.repositories.IotEventRepository
import reactor.core.publisher.Mono
import java.util.*
import java.util.logging.Logger

@Component
class RouterHandler(
        val iotRepository: IoTRepository,
        val iotEventRepository: IotEventRepository){

    companion object {
        val logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun getAll(serverRequest: ServerRequest): Mono<ServerResponse> {
/*
        val collectList = iotRepository.findAll()
                .map { iot ->
                    iotEventRepository.findOneByIotIdOrderByDateDesc(iot.id)
                }.toFlux()
*/

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
                .flatMap {
                    iotRepository.findById(it.id)
                }
                .flatMap {
                    iotDevice.flatMap {
                        val errorMsg = String.format("Device exists with ID: %s", it.id)
                        logger.info(errorMsg)
                        ServerResponse.badRequest().syncBody(errorMsg)
                    }
                }
                .switchIfEmpty(
                        iotDevice.flatMap {
                            logger.info(String.format("New device was added with ID: %s", it.id))
                            ServerResponse.ok().body(iotRepository.save(it))
                        }
                )
    }

    fun getEventById(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id: String = serverRequest.pathVariable("id")
        return ServerResponse.ok()
                .body(iotEventRepository.findByIotIdOrderByDateDesc(id))
    }

    fun event(serverRequest: ServerRequest): Mono<ServerResponse> {
        val iotDevice = serverRequest.body(toMono(TemperatureSensor::class.java)).cache()
        var temp = 0F;

        return iotDevice
                .flatMap {
                    temp = it.temp
                    iotRepository.findById(it.id)
                }.flatMap {
                    val iot = TemperatureSensor(it.id, temp)
                    val event = IoTEvent(iot, Date())

                    logger.info(String.format("Updating device ID: %s", it.id))
                    ServerResponse.ok().body(iotEventRepository.save(event))
                }
                .switchIfEmpty(
                        ServerResponse.badRequest().syncBody("Temperature sensor data is not valid for device: ${iotDevice}")
                )

    }

    fun remove(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id: String = serverRequest.pathVariable("id")
        return ServerResponse.ok()
                .body(iotRepository.deleteById(id))
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