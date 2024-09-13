package com.ct07.ttn.models

import java.io.Serializable

data class Room(
    val address: Address,
    val area: String,
    val author: String,
    val phone_number: String,
    val deposits: Int,
    val description: String,
    val id: Int,
    val image: Image,
    val interior: Int,
    val price: Int,
    val title: String,
    val upload_time: String
): Serializable