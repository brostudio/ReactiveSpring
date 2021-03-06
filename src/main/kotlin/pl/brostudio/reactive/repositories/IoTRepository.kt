package pl.brostudio.reactive.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import pl.brostudio.reactive.entities.TemperatureSensor

interface IoTRepository: ReactiveMongoRepository<TemperatureSensor, String>