package pl.brostudio.reactive.resource;

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.brostudio.reactive.entities.Employee
import pl.brostudio.reactive.entities.EmployeeEvent
import pl.brostudio.reactive.repositories.EmployeeRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*
import java.util.stream.Stream

@RestController
@RequestMapping("/rest/employee")
class EmployeeResource(val employeeRepository: EmployeeRepository) {

    @GetMapping("/all")
    fun getAll(): Flux<Employee> = employeeRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: String): Mono<Employee> {
        return employeeRepository.findById(id);
    }

    @GetMapping("/{id}/events", produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
    fun getEmployeeEvents(@PathVariable("id") id: String): Flux<EmployeeEvent> {
        return employeeRepository.findById(id)
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
    }

}
