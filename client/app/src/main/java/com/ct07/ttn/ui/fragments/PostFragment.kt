package com.ct07.ttn.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ct07.ttn.R
import com.ct07.ttn.databinding.FragmentPostBinding
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.room.RoomViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ct07.ttn.models.User
import com.ct07.ttn.ui.user.UserViewModel
import com.google.firebase.storage.FirebaseStorage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.wait
import java.io.ByteArrayOutputStream
import kotlin.math.log

class PostFragment : Fragment(R.layout.fragment_post), OnAddressSelectedListener {

    lateinit var binding: FragmentPostBinding
    lateinit var roomViewModel: RoomViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var navController: NavController
    lateinit var itemAskLogin: CardView

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageByteArray: ByteArray? = null
    private var road: String? = null
    private var ward: String? = null
    private var district: String? = null
    private var province: String? = null
    private var addressID: Int ?= -1
    private var roomID: Int ?= -1
    private var uri_image: Uri ?= null
    private var user: User?= null

    private var loadAddress: Boolean = false
    private var loadRoom: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostBinding.bind(view)

        navController = Navigation.findNavController(view)
        roomViewModel = (activity as RoomActivity).roomViewModel
        userViewModel = (activity as RoomActivity).userViewModel

        if (!userViewModel.isLogin()){
            itemAskLogin = view.findViewById(R.id.itemAskLogin)
            itemAskLogin.visibility = View.VISIBLE
            hidePost()
        } else {
            user = userViewModel.getUser()
        }

        binding.addAddress.setOnClickListener {
            // Add address
            val dialog = AddressDialogFragment(this)
            dialog.show(childFragmentManager, "AddressDialogFragment")
        }

        binding.addImage.setOnClickListener {
            // Add image
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST)
        }

        binding.btnAddPost.setOnClickListener {
            val title = binding.addTitle.text.toString()
            val description = binding.addDescription.text.toString()
            val area = binding.addArea.text.toString()
            val price = binding.addPrice.text.toString()
            val interior = binding.addInterior.isChecked
            val deposits = binding.addDeposits.text.toString()


            // Check if image is selected
            if (selectedImageByteArray == null) {
                Toast.makeText(activity, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if all fields are filled
            if(title.isEmpty() || description.isEmpty() || area.isEmpty() || price.isEmpty() || deposits.isEmpty()){
                Toast.makeText(activity, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }else{
                // Add address
                roomViewModel.addAddress(province!!, district!!, ward!!, road!!)
                roomViewModel.address_id.observe(viewLifecycleOwner, Observer { addressId ->
                    if (addressId != null && addressId != -1) {
                        addressID = addressId
                        loadAddress = true

                        binding.paginationProgressBar?.visibility = View.VISIBLE
                        roomViewModel.addRoom(title, user?.id ?: -1, addressID ?: -1, description, area.toInt(), price.toInt(), interior,
                            deposits.toInt())
                        roomViewModel.room_id.observe(viewLifecycleOwner, Observer { roomId ->
                            Log.d("RoomID: ", roomId.toString())
                            if (roomId != null && roomId != -1) {
                                roomID = roomId
                                loadRoom = true
                                // Tải ảnh lên Firebase
                                val storageRef = FirebaseStorage.getInstance().reference
                                val imageRef = storageRef.child("images/${uri_image?.lastPathSegment}")
                                val uploadTask = imageRef.putFile(uri_image?: Uri.EMPTY)

                                uploadTask.addOnSuccessListener {
                                    imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                        roomViewModel.addImage(roomID?: -1, downloadUrl.toString())
                                        Log.d("DownloadURL: ", downloadUrl.toString())
                                        binding.paginationProgressBar?.visibility = View.GONE
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(activity, "Tải ảnh lên thất bại", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(activity, "Đã có lỗi trong quá trình đăng bài, vui lòng thử lại sau!",
                                    Toast.LENGTH_SHORT).show()
                            }
                            navController.navigate(R.id.headlinesFragment)
                            Toast.makeText(activity, "Đăng bài thành công", Toast.LENGTH_SHORT).show()
                        })


                    } else {
                        Toast.makeText(activity, "Đã có lỗi trong quá trình đăng bài, vui lòng thử lại sau!",
                            Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            uri_image = data.data
            if (uri_image != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri_image)
                val drawable = BitmapDrawable(resources, bitmap)
                binding.addImage.setImageDrawable(drawable)
                binding.addImage.adjustViewBounds = true

                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                selectedImageByteArray = stream.toByteArray()
            }
        }
    }

    override fun onAddressSelected(province: String, district: String, ward: String, road: String) {
        binding.addAddress.setText("$road, $ward, $district, $province")

        this.road = road
        this.ward = ward
        this.district = district
        this.province = province
    }

    private fun hidePost(){
        binding.addAddress.visibility = View.GONE
        binding.addImage.visibility = View.GONE
        binding.addTitle.visibility = View.GONE
        binding.addDescription.visibility = View.GONE
        binding.addArea.visibility = View.GONE
        binding.addPrice.visibility = View.GONE
        binding.addInterior.visibility = View.GONE
        binding.addDeposits.visibility = View.GONE
        binding.btnAddPost.visibility = View.GONE
        binding.txtInformation.visibility = View.GONE
        binding.txtTitleDescription.visibility = View.GONE
        binding.txtPriceArea.visibility = View.GONE
    }
}

interface OnAddressSelectedListener {
    fun onAddressSelected(province: String, district: String, ward: String, road: String)
}