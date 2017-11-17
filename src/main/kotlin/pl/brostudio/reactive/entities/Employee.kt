package pl.brostudio.reactive.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Employee (@Id var id:String, name:String, salary:Long) {
}