package com.ct07.ttn.repository

import com.ct07.ttn.api.RetrofitInstance
import com.ct07.ttn.api.RoomAPI
import com.ct07.ttn.models.User

class UserRepository(private val api: RoomAPI) {

    suspend fun login(phoneNumber: String, password: String) =
        RetrofitInstance.api.login(phoneNumber, password)

    suspend fun register(user: User) =
        RetrofitInstance.api.register(user.username, user.phone_number, user.password)

    suspend fun checkPhoneNumber(phoneNumber: String) =
        RetrofitInstance.api.checkPhoneNumber(phoneNumber)
}