package pl.brostudio.reactive.entities

import java.util.*

data class IoTEvent<T : IoT> (val iot:T, val date: Date)