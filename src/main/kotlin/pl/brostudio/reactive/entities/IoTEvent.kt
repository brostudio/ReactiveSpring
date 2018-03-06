package pl.brostudio.reactive.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class IoTEvent (@Id val iot:TemperatureSensor, val date: Date)