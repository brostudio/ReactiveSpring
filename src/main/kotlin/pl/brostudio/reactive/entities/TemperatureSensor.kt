package pl.brostudio.reactive.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class TemperatureSensor (@Id val id:String, val temp:Float) : IoT