package com.ct07.ttn.ui.user

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ct07.ttn.models.User
import com.ct07.ttn.models.UserResponse
import com.ct07.ttn.repository.UserRepository
import com.ct07.ttn.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UserViewModel(app: Application, val userRepository: UserRepository): AndroidViewModel(app){

    val users: MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    var userResponse: UserResponse? = null

    fun login(username: String, password: String) = viewModelScope.launch {
        usersInternet(username, password)
    }

    fun register(user: User) = viewModelScope.launch {
        registerInternet(user)
    }

    fun checkPhoneNumber(phoneNumber: String): Boolean {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            result.value = checkPhoneNumberInternet(phoneNumber)
        }
        return result.value ?: false
    }

    fun isLogin(): Boolean{
        return userResponse != null
    }

    fun logout(){
        userResponse = null
        users.postValue(Resource.Success(userResponse ?: UserResponse()))
    }

    fun getUser(): User? {
        return if (userResponse?.isNotEmpty() == true) {
            userResponse?.get(0)
        } else {
            null
        }
    }

    private fun handleUsersResponse(response: Response<UserResponse>): Resource<UserResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                userResponse = resultResponse
                if(userResponse == null){
                    return Resource.Error("Tài khoản hoặc mật khẩu không tồn tại")
                }
                return Resource.Success(userResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRegisterResponse(response: Response<UserResponse>): Resource<UserResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                if(userResponse == null){
                    userResponse = resultResponse
                    return Resource.Error("Số điện thoại đã tồn tại")
                }
                return Resource.Success(userResponse ?: resultResponse)
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
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun usersInternet(username: String, password: String){
        try {
            if(hasInternetConnection(getApplication())){
                val response = userRepository.login(username, password)
                if(response.isSuccessful){
                    if(response.body()?.isNotEmpty() == true){
                        users.postValue(handleUsersResponse(response))
                    } else {
                        users.postValue(Resource.Error("Tài khoản hoặc mật khẩu không tồn tại"))
                    }
                } else {
                    users.postValue(Resource.Error(response.message()))
                }
            } else {
                users.postValue(Resource.Error("Không có kết nối internet"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> users.postValue(Resource.Error("Lỗi mạng"))
                else -> users.postValue(Resource.Error("Lỗi chuyển đổi"))
            }
        }
    }

    private suspend fun registerInternet(user: User){
        users.postValue(Resource.Loading())
        try {
            if(hasInternetConnection(getApplication())){
                val response = userRepository.register(user)
                users.postValue(handleRegisterResponse(response))
            } else {
                users.postValue(Resource.Error("Không có kết nối internet"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> users.postValue(Resource.Error("Lỗi mạng"))
                else -> users.postValue(Resource.Error("Lỗi chuyển đổi"))
            }
        }
    }

    private suspend fun checkPhoneNumberInternet(phoneNumber: String): Boolean {
        return try {
            if(hasInternetConnection(getApplication())){
                val response = userRepository.checkPhoneNumber(phoneNumber)
                response.isSuccessful && response.body()?.isNotEmpty() == true
            } else {
                false
            }
        } catch (t: Throwable){
            false
        }
    }
}