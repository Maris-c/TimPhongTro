package com.ct07.ttn.ui.room

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ct07.ttn.models.*
import com.ct07.ttn.repository.RoomRepository
import com.ct07.ttn.util.Resource
import com.google.android.play.core.integrity.t
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.IOException

class RoomViewModel(app: Application, val roomRepository: RoomRepository): AndroidViewModel(app){

    val headlines: MutableLiveData<Resource<RoomResponse>> = MutableLiveData()
    val favorites: MutableLiveData<Resource<RoomResponse>> = MutableLiveData()
    var address_id: MutableLiveData<Int?> = MutableLiveData()
    var room_id: MutableLiveData<Int?> = MutableLiveData()
    var headlinesPage = 1;
    var headlinesResponse: RoomResponse? = null
    var favoritesResponse: RoomResponse? = null

    // Address
    val provinces: MutableLiveData<Resource<ProvinceResponse>> = MutableLiveData()
    val districts: MutableLiveData<Resource<DistrictResponse>> = MutableLiveData()
    val wards: MutableLiveData<Resource<WardResponse>> = MutableLiveData()

    init {
        getHeadlines()
    }

    fun getHeadlines() = viewModelScope.launch {
        headlinesInternet()
    }

    fun getProvince() = viewModelScope.launch {
        provinceInternet()
    }

    fun getDistricts(province: String) = viewModelScope.launch {
        districtsInternet(province)
    }

    fun getWards(district: String) = viewModelScope.launch {
        wardsInternet(district)
    }

    fun searchRoom(query: String) = viewModelScope.launch {
        searchRoomInternet(query)
    }

    fun searchRoomByProvince(query: String) = viewModelScope.launch {
        searchRoomByProvinceInternet(query)
    }

    fun searchRoomByDistrict(query: String) = viewModelScope.launch {
        searchRoomByDistrictInternet(query)
    }

    fun searchRoomByWard(query: String) = viewModelScope.launch {
        searchRoomByWardInternet(query)
    }

    fun addRoom(title: String, author: Int, address_id: Int, description: String, area: Int, price: Int,
                interior: Boolean, deposits: Int) = viewModelScope.launch {
        addRoomInternet(title, author, address_id, description, area, price, interior, deposits)
    }

    fun getFavorites(user_id: Int) = viewModelScope.launch {
        getFavoritesInternet(user_id)
    }

    fun addToFavorites(user_id: Int, room_id: Int) = viewModelScope.launch {
        addFavoritesInternet(user_id, room_id)
    }

    fun unFavorites(user_id: Int, room_id: Int) = viewModelScope.launch {
        unFavoritesInternet(user_id, room_id)
    }

    fun addAddress(province: String, district: String, ward: String, road: String) = viewModelScope.async {
        addAddressInternet(province, district, ward, road)
    }

    fun addImage(room_id: Int, uri_image: String) = viewModelScope.launch {
        addImageInternet(room_id, uri_image)
    }

    // chèn response
    private fun handleHeadlinesResponse(response: Response<RoomResponse>): Resource<RoomResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                headlinesPage++
                if(headlinesResponse == null){
                    headlinesResponse = resultResponse
                } else {
                    val oldRoom = headlinesResponse
                    val newRoom = resultResponse
                    oldRoom?.addAll(newRoom)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleFavoritesResponse(response: Response<RoomResponse>): Resource<RoomResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                if(favoritesResponse == null){
                    favoritesResponse = resultResponse
                } else {
                    val oldRoom = favoritesResponse
                    val newRoom = resultResponse
                    oldRoom?.addAll(newRoom)
                }
                return Resource.Success(favoritesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleProvinceResponse(response: Response<ProvinceResponse>): Resource<ProvinceResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleDistrictResponse(response: Response<DistrictResponse>): Resource<DistrictResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleWardResponse(response: Response<WardResponse>): Resource<WardResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun hasInternetConnection(context: Context): Boolean{
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                    else -> return false
                }
            } ?: false
        }
    }

    private suspend fun headlinesInternet(){
        headlines.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(this.getApplication())){
                val response = roomRepository.getHeadlines(headlinesPage)
                headlines.postValue(handleHeadlinesResponse(response))
            } else {
                headlines.postValue(Resource.Error("Không có kết nối internet"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> headlines.postValue(Resource.Error("Lỗi mạng"))
                else -> headlines.postValue(Resource.Error("Lỗi chuyển đổi"))
            }
        }
    }

    private suspend fun getFavoritesInternet(user_id: Int){
        favorites.postValue(Resource.Loading())
        favoritesResponse = null
        try{
            if(hasInternetConnection(this.getApplication())){
                val response = roomRepository.getFavorites(user_id)
                favorites.postValue(handleFavoritesResponse(response))
                } else {
                    favorites.postValue(Resource.Error("Không có kết nối internet"))
                }
        } catch (t: Throwable){
            when(t){
                is IOException -> favorites.postValue(Resource.Error("Lỗi mạng"))
                else -> favorites.postValue(Resource.Error("Lỗi chuyển đổi"))
            }

        }
    }
    private suspend fun provinceInternet(){
        val response = roomRepository.getProvince()
        provinces.postValue(handleProvinceResponse(response))
    }

    private suspend fun districtsInternet(province: String){
        val response = roomRepository.getDistricts(province)
        districts.postValue(handleDistrictResponse(response))
    }

    private suspend fun wardsInternet(district: String){
        val response = roomRepository.getWards(district)
        wards.postValue(handleWardResponse(response))
    }

    private suspend fun searchRoomInternet(query: String){
        val response = roomRepository.searchRoom(query)
        if (response.isSuccessful) {
            response.body()?.let { searchRoomResponse ->
                headlines.postValue(Resource.Success(searchRoomResponse))
            }
        } else {
            headlines.postValue(Resource.Error(response.message()))
        }
    }

    private suspend fun searchRoomByProvinceInternet(query: String){
        val response = roomRepository.searchRoomByProvince(query)
        if (response.isSuccessful) {
            response.body()?.let { searchRoomResponse ->
                headlines.postValue(Resource.Success(searchRoomResponse))
            }
        } else {
            headlines.postValue(Resource.Error(response.message()))
        }
    }

    private suspend fun searchRoomByDistrictInternet(query: String){
        val response = roomRepository.searchRoomByDistrict(query)
        if (response.isSuccessful) {
            response.body()?.let { searchRoomResponse ->
                headlines.postValue(Resource.Success(searchRoomResponse))
            }
        } else {
            headlines.postValue(Resource.Error(response.message()))
        }
    }

    private suspend fun searchRoomByWardInternet(query: String){
        val response = roomRepository.searchRoomByWard(query)
        if (response.isSuccessful) {
            response.body()?.let { searchRoomResponse ->
                headlines.postValue(Resource.Success(searchRoomResponse))
            }
        } else {
            headlines.postValue(Resource.Error(response.message()))
        }
    }

    private suspend fun addRoomInternet(
        title: String, author: Int, address_id: Int, description: String, area: Int, price: Int,
        interior: Boolean, deposits: Int){
        val response = roomRepository.addRoom(title, author, address_id, description, area, price, interior, deposits)
        if (response.isSuccessful) {
            if (response.body()?.id != null) {
                room_id.postValue(response.body()?.id)
            } else {
                room_id.postValue(-1)
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Lỗi khác khi tạo phòng",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun addFavoritesInternet(user_id: Int, room_id: Int){
        try {
            if (hasInternetConnection(getApplication())) {
                val response = roomRepository.addToFavorites(user_id, room_id)
            } else {
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Không có kết nối internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Lỗi mạng",
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Lỗi chuyển đổi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun unFavoritesInternet(user_id: Int, room_id: Int) {
        try {
            if (hasInternetConnection(getApplication())) {
                val response = roomRepository.unFavorites(user_id, room_id)
            } else {
                Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Không có kết nối internet",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Lỗi mạng",
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(
                    getApplication<Application>().applicationContext,
                    "Lỗi chuyển đổi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun addAddressInternet(province: String, district: String, ward: String, road: String) {
        val response = roomRepository.addAddress(province, district, ward, road)
        if (response.isSuccessful) {
            if (response.body()?.id != null) {
                address_id.postValue(response.body()?.id)
            } else {
                address_id.postValue(-1)
                Toast.makeText(getApplication<Application>().applicationContext, "Đã có lỗi trong quá trình tạo địa chỉ, vui lòng thử lại sau!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun addImageInternet(room_id: Int, uri_image: String) {
        roomRepository.addImage(room_id, uri_image)
    }
}