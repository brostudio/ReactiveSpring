package pl.brostudio.reactive.config

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import pl.brostudio.reactive.entities.Employee
import pl.brostudio.reactive.entities.EmployeeEvent
import pl.brostudio.reactive.repositories.EmployeeRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*
import java.util.stream.Stream

@Component
class RouterHandler(val employeeRepository: EmployeeRepository) {
    fun getAll(serverRequest: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
                .body(employeeRepository.findAll(), Employee::class.java)
    }

    fun getId(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id: String = serverRequest.pathVariable("id")
        return ServerResponse.ok()
                .body(employeeRepository.findById(id), Employee::class.java)
    }

    fun getEvents(serverRequest: ServerRequest): Mono<ServerResponse> {
        val id: String = serverRequest.pathVariable("id")
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                        employeeRepository.findById(id)
                                .flatMapMany { employee ->
                                    val interval = Flux.interval(Duration.ofSeconds(2))

                                    val employeeEventFlux = Flux.fromStream(
                                            Stream.generate({
                                                EmployeeEvent(employee, Date())
                                            })
                                    )

                                    Flux.zip(interval, employeeEventFlux)
                                            .map { o -> o.t2 }
                                }
                        , EmployeeEvent::class.java)
    }
}