package com.ct07.ttn.api

import com.ct07.ttn.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface RoomAPI {

    // Room
    @GET("rooms")
    suspend fun getHeadlines(
        @Query("page")
        pageNumber: Int = 1
    ): Response<RoomResponse>

    @GET("searchRoom")
    suspend fun searchRoom(
        @Query("query")
        query: String
    ): Response<RoomResponse>

    @GET("searchRoom/province")
    suspend fun searchRoomByProvince(
        @Query("query")
        query: String
    ): Response<RoomResponse>

    @GET("searchRoom/district")
    suspend fun searchRoomByDistrict(
        @Query("query")
        query: String
    ): Response<RoomResponse>

    @GET("searchRoom/ward")
    suspend fun searchRoomByWard(
        @Query("query")
        query: String
    ): Response<RoomResponse>

    // Address
    @GET("provinces")
    suspend fun getProvince(): Response<ProvinceResponse>

    @GET("districts")
    suspend fun getDistricts(
        @Query("province")
        province: String
    ): Response<DistrictResponse>

    @GET("wards")
    suspend fun getWards(
        @Query("district")
        district: String
    ): Response<WardResponse>


    @FormUrlEncoded
    @POST("address/add")
    suspend fun addAddress(
        @Field("province") province: String,
        @Field("district") district: String,
        @Field("ward") ward: String,
        @Field("road") road: String
    ): Response<AddResponse>

    @FormUrlEncoded
    @POST("rooms/add")
    suspend fun addRoom(
        @Field("title") title: String,
        @Field("author") author: Int,
        @Field("address_id") address_id: Int,
        @Field("description") description: String,
        @Field("area") area: Int,
        @Field("price") price: Int,
        @Field("interior") interior: Boolean,
        @Field("deposits") deposits: Int
    ): Response<AddResponse>

    // Favorites
    @GET("favorites")
    suspend fun getFavorites(
        @Query("user_id")
        user_id: Int
    ): Response<RoomResponse>

    @FormUrlEncoded
    @POST("favorites/add")
    suspend fun addToFavorites(
        @Field("user_id") user_id: Int,
        @Field("room_id") room_id: Int
    )

    @FormUrlEncoded
    @POST("favorites/remove")
    suspend fun unFavorites(
        @Field("user_id") user_id: Int,
        @Field("room_id") room_id: Int
    )

    // User
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("users/checkPhoneNumber")
    suspend fun checkPhoneNumber(
        @Field("phoneNumber") phoneNumber: String
    ): Response<UserResponse>

    // Image
    @FormUrlEncoded
    @POST("images/add")
    suspend fun addImage(
        @Field("room_id") user_id: Int,
        @Field("uri_image") image: String
    )
}