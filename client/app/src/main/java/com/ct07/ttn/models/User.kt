package com.ct07.ttn.models

import java.io.Serializable

data class User(
    val favorite_room: Any,
    val id: Int,
    val level: String,
    val password: String,
    val phone_number: String,
    val username: String
): Serializable