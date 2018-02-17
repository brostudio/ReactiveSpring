package pl.brostudio.reactive.config

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import pl.brostudio.reactive.entities.TemperatureSensor
import pl.brostudio.reactive.repositories.IoTRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.toMono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(RouterHandler::class)
class RouterConfigTest {

    @Autowired
    lateinit var routerHandler: RouterHandler

    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var employeeRepository: IoTRepository<TemperatureSensor>

    val testSensorsData = listOf(
            TemperatureSensor(UUID.randomUUID().toString(), 21.3F),
            TemperatureSensor(UUID.randomUUID().toString(), 22.5F),
            TemperatureSensor(UUID.randomUUID().toString(), 25.2F)
    )

    @Before
    fun init() {
        webTestClient = WebTestClient.bindToRouterFunction(ReactiveConfig::routerFunction).build();
        //employeeRepository.deleteAll().block();
    }

    @Test
    fun shouldFindAllEmployees() {
        given(employeeRepository.findAll()).willReturn(Flux.fromIterable(testSensorsData))
        webTestClient.get()
                .uri("/iot/all")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("John Smith")
                .jsonPath("$.length()").isEqualTo(testSensorsData.size)
    }

    @Test
    fun shouldFindEmployeeBySpecificId() {
        given(employeeRepository.findById(ArgumentMatchers.anyString())).willReturn(testSensorsData.first().toMono())
        webTestClient.get()
                .uri("/iot/{id}", testSensorsData.first().id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
    }

/*
    @Test
    fun shouldAddDevice() {
        webTestClient.put()
                .uri("/iot/addDevice")
                .
    }
*/

}