package pl.brostudio.reactive.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import pl.brostudio.reactive.entities.IoTEvent
import pl.brostudio.reactive.entities.TemperatureSensor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IotEventRepository: ReactiveMongoRepository<IoTEvent, TemperatureSensor> {
    fun findOneByIotIdOrderByDateDesc(id:String): Mono<IoTEvent>

    fun findByIotIdOrderByDateDesc(id:String): Flux<IoTEvent>
}