package pl.brostudio.reactive.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import pl.brostudio.reactive.entities.IoT
import reactor.core.publisher.Mono

interface IoTRepository<T:IoT>: ReactiveMongoRepository<T, String> {
}