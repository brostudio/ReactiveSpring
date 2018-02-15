package pl.brostudio.reactive.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import pl.brostudio.reactive.entities.IoT

interface IoTRepository: ReactiveMongoRepository<IoT, String>