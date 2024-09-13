package com.ct07.ttn.repository

import com.ct07.ttn.api.RetrofitInstance
import com.ct07.ttn.api.RoomAPI
import com.ct07.ttn.models.Province
import okhttp3.MultipartBody

class RoomRepository(private val api: RoomAPI) {
    suspend fun getHeadlines(pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(pageNumber)

    suspend fun getProvince() = RetrofitInstance.api.getProvince()

    suspend fun getDistricts(province: String) = RetrofitInstance.api.getDistricts(province)

    suspend fun getWards(district: String) = RetrofitInstance.api.getWards(district)

    suspend fun searchRoom(query: String) = RetrofitInstance.api.searchRoom(query)

    suspend fun searchRoomByProvince(query: String) = RetrofitInstance.api.searchRoomByProvince(query)

    suspend fun searchRoomByDistrict(query: String) = RetrofitInstance.api.searchRoomByDistrict(query)

    suspend fun searchRoomByWard(query: String) = RetrofitInstance.api.searchRoomByWard(query)

    suspend fun addRoom(title: String, author: Int, address_id: Int, description: String, area: Int, price: Int,
                interior: Boolean, desposits: Int) = RetrofitInstance.api.addRoom(
        title, author, address_id, description, area, price, interior, desposits)

    suspend fun getFavorites(user_id: Int) = RetrofitInstance.api.getFavorites(user_id)

    suspend fun addToFavorites(user_id: Int, room_id: Int) = RetrofitInstance.api.addToFavorites(user_id, room_id)

    suspend fun unFavorites(user_id: Int, room_id: Int) = RetrofitInstance.api.unFavorites(user_id, room_id)

    suspend fun addAddress(province: String, district: String, ward: String, road: String) = RetrofitInstance.api.addAddress(province, district, ward, road)

    //image
    suspend fun addImage(room_id: Int, uri_image: String) = RetrofitInstance.api.addImage(room_id, uri_image)
}