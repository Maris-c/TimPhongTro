package com.ct07.ttn.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ct07.ttn.R
import com.ct07.ttn.api.RetrofitInstance.Companion.api
import com.ct07.ttn.databinding.ActivityRoomBinding
import com.ct07.ttn.repository.RoomRepository
import com.ct07.ttn.repository.UserRepository
import com.ct07.ttn.ui.room.RoomViewModel
import com.ct07.ttn.ui.room.RoomViewModelProviderFactory
import com.ct07.ttn.ui.user.UserViewModel
import com.ct07.ttn.ui.user.UserViewModelProviderFactory

class RoomActivity : AppCompatActivity() {

    lateinit var roomViewModel: RoomViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var binding: ActivityRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val roomRepository = RoomRepository(api)
        val viewModelProviderFactory = RoomViewModelProviderFactory(application, roomRepository)
        roomViewModel = ViewModelProvider(this, viewModelProviderFactory).get(RoomViewModel::class.java)

        val userRepository = UserRepository(api)
        val userViewModelProviderFactory = UserViewModelProviderFactory(application, userRepository)
        userViewModel = ViewModelProvider(this, userViewModelProviderFactory).get(UserViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.roomNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}