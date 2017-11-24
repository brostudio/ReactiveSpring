package pl.brostudio.reactive.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Employee (@Id val id:String, var name:String, var salary:Long)