package com.ct07.ttn.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ct07.ttn.R
import com.ct07.ttn.databinding.FragmentRoomBinding
import com.ct07.ttn.models.User
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.room.RoomViewModel
import com.ct07.ttn.ui.user.UserViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class RoomFragment : Fragment(R.layout.fragment_room) {

    lateinit var roomViewModel: RoomViewModel
    lateinit var userViewModel: UserViewModel
    val args: RoomFragmentArgs by navArgs()
    lateinit var binding: FragmentRoomBinding
    private var user: User?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentRoomBinding.bind(view)

            roomViewModel = (activity as RoomActivity).roomViewModel
            userViewModel = (activity as RoomActivity).userViewModel
            val room = args.room

            //Ảnh
            if (room.image.uri_image != null && room.image.uri_image.isNotEmpty()) {
                Glide.with(this).load(room.image.uri_image[0].toUri()).into(binding.roomImage)
            }

            binding.roomTitle.text = room.title
            val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
            binding.roomPrice.text = "Giá: " + numberFormat.format(room.price)
            binding.roomAddress.text = "Địa chỉ: " + room.address.road + ", " + room.address.ward + ", " + room.address.district + ", " + room.address.province
            binding.roomArea.text = room.area + " m2"

            binding.roomInterior.text = if (room.interior == 1) "Đầy đủ" else "Trống"
            binding.roomDeposits.text = numberFormat.format(room.deposits)
            binding.roomDescription.text = room.description

            binding.roomAuthor.text = room.author
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val uploadDate = inputFormat.parse(room.upload_time)
            val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val uploadDateString = outputFormat.format(uploadDate)
            binding.roomDateTime.text = "Thời gian đăng bài: " + uploadDateString


            binding.phone.setOnClickListener {
                val dial = "tel:${room.phone_number}"
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(dial)))
            }
        } catch (e: Exception) {
            Log.e("RoomFragment", "Error in onViewCreated", e)
        }
    }

}