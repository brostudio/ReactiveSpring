package pl.brostudio.reactive.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import pl.brostudio.reactive.repositories.IoTRepository

@Controller
class IndexController(val iotRepository: IoTRepository) {

    @RequestMapping("/")
    fun index(model: Model):String {
        model.addAttribute("iotDevicesCount", iotRepository.count())
        return "index"
    }
}