package com.ct07.ttn.ui.room

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ct07.ttn.repository.RoomRepository

class RoomViewModelProviderFactory(val app: Application, val roomRepository: RoomRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoomViewModel(app, roomRepository) as T
    }
}