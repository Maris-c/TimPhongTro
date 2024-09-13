package com.ct07.ttn.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ct07.ttn.R
import com.ct07.ttn.adapters.RoomAdapter
import com.ct07.ttn.databinding.FragmentFavouritesBinding
import com.ct07.ttn.models.User
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.room.RoomViewModel
import com.ct07.ttn.ui.user.UserViewModel
import com.ct07.ttn.util.Resource


class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private lateinit var roomViewModel: RoomViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var itemAskLogin: CardView
    private var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)

        // Khởi tạo roomViewModel sử dụng ViewModelProvider
        roomViewModel = (activity as RoomActivity).roomViewModel
        userViewModel = (activity as RoomActivity).userViewModel
        setupFavouritesRecycler()

        if (!userViewModel.isLogin()){
            itemAskLogin = view.findViewById(R.id.itemAskLogin)
            itemAskLogin.visibility = View.VISIBLE
            hideFavorite()
        } else {
            user = userViewModel.getUser()
        }

        roomAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("room", it)
            }
            findNavController().navigate(R.id.action_favouritesFragment_to_roomFragment, bundle)
        }

        user?.let {
          roomViewModel.getFavorites(it.id)
        }

        user?.let {
            roomViewModel.favorites.observe(viewLifecycleOwner, Observer { response ->
                when(response){
                    is Resource.Success<*> -> {
                        response.data?.let {roomResponse ->
                            roomAdapter.differ.submitList(roomResponse.toList())
                        }
                    }
                    is Resource.Error<*> -> {
                        response.message?.let { message ->
                            // Show error message
                        }
                    }
                    is Resource.Loading<*> -> {
                        // Show progress bar
                    }
                }
            })
        }
    }

    private fun setupFavouritesRecycler() {
        roomAdapter = RoomAdapter(roomViewModel, userViewModel, viewLifecycleOwner)
        binding.recyclerFavorites.apply {
            adapter = roomAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideFavorite() {
        binding.recyclerFavorites.visibility = View.GONE
    }
}