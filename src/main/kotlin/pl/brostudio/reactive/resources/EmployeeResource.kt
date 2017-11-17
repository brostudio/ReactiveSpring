package pl.brostudio.reactive.resource;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.brostudio.reactive.entities.Employee
import pl.brostudio.reactive.repositories.EmployeeRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/rest/employee")
class EmployeeResource(val employeeRepository: EmployeeRepository) {

    @GetMapping("/all")
    fun getAll(): Flux<Employee> = employeeRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id:String): Mono<Employee> {
        return employeeRepository.findById(id);
    }
}
