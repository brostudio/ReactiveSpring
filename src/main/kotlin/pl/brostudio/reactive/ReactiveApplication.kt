package pl.brostudio.reactive

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pl.brostudio.reactive.entities.Employee
import pl.brostudio.reactive.repositories.EmployeeRepository
import java.util.*
import java.util.stream.Stream

@SpringBootApplication
class ReactiveApplication

fun main(args: Array<String>) {
    @Bean
    fun addInitialEmployees(employeeRepository: EmployeeRepository) = CommandLineRunner {
        employeeRepository.deleteAll()
                .subscribe(null, null, {
                    Stream.of(
                            Employee(UUID.randomUUID().toString(), "Marcin Nowakowski", 100_000L),
                            Employee(UUID.randomUUID().toString(), "Marcin Nowakowski", 100_000L),
                            Employee(UUID.randomUUID().toString(), "Marcin Nowakowski", 100_000L)
                    ).forEach { employee ->
                        {
                            employeeRepository
                                    .save(employee)
                                    .subscribe(System.out::println)
                        }
                    }
                })
    }

    runApplication<ReactiveApplication>(*args)
}
