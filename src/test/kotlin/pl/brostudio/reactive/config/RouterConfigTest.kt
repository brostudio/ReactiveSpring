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
import org.springframework.web.reactive.function.BodyInserters
import pl.brostudio.reactive.entities.TemperatureSensor
import pl.brostudio.reactive.repositories.IoTRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.toMono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(RouterHandler::class)
class RouterConfigTest {

    @Autowired
    lateinit var context:ApplicationContext

    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var employeeRepository: IoTRepository

    val testSensorsData = listOf(
            TemperatureSensor(UUID.randomUUID().toString(), 21.3F),
            TemperatureSensor(UUID.randomUUID().toString(), 22.5F),
            TemperatureSensor(UUID.randomUUID().toString(), 25.2F)
    )

    @Before
    fun init() {
        webTestClient = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .baseUrl("http://localhost:8080/")
                .build()
        //employeeRepository.deleteAll().block();
    }

    @Test
    fun shouldFindAllEmployees() {
        given(employeeRepository.findAll()).willReturn(Flux.fromIterable(testSensorsData))
        webTestClient.get()
                .uri("/iot/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$[0].temp").isEqualTo(testSensorsData.first().temp)
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

    @Test
    fun shouldNotAddDeviceWhenExists() {
        webTestClient.post()
                .uri("/iot/addDevice")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(testSensorsData.first().toMono()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$[0].temp").isEqualTo(testSensorsData.first().temp)
                .jsonPath("$[0].id").isEqualTo(testSensorsData.first().id)
    }

    @Test
    fun shouldRemoveDeviceWhenExists() {
        webTestClient.delete()
                .uri("/iot/remove/{id}", testSensorsData.first().id)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("$[0].temp").isEqualTo(testSensorsData.first().temp)
                .jsonPath("$[0].id").isEqualTo(testSensorsData.first().id)
    }


/*
    @Test
    fun shouldAddDevice() {
        webTestClient.put()
                .uri("/iot/addDevice")
                .body(BodyInserters.fromObject(testSensorsData.furst().toMono()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TemperatureSensor::class.java)
                .isEqualTo(testSensorsData.last())
    }
*/

}