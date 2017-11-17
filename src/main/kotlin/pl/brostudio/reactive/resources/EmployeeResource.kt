package pl.brostudio.reactive.resource;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.brostudio.reactive.entities.Employee

@RestController
@RequestMapping("/rest/employee")
class EmployeeResource {

    @GetMapping("/all")
    fun getAll():List<Employee> {

        return ArrayList<Employee>();
    }

}
