package com.ct07.ttn.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ct07.ttn.R
import com.ct07.ttn.databinding.FragmentAccountBinding
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.user.UserViewModel
import com.ct07.ttn.util.Resource

class AccountFragment : Fragment(R.layout.fragment_account), OnUsernameReceivedListener {

    lateinit var userViewModel: UserViewModel
    lateinit var binding: FragmentAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

        userViewModel = (activity as RoomActivity).userViewModel
        binding.btnLogout.visibility = View.GONE

        userViewModel.users.observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    if (resource.data?.isNotEmpty() == true) {
                        binding.btnLogin.visibility = View.GONE
                        binding.btnLogout.visibility = View.VISIBLE
                        binding.txtWellcome.text = "Hello ${resource.data[0].username}!"
                    }
                }
                Resource.Status.ERROR -> {
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.btnLogout.visibility = View.GONE
                    binding.txtWellcome.text = "Vui lòng Đăng Nhập!"
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
                Resource.Status.LOADING -> {
                }
            }
        })

        binding.btnLogin.setOnClickListener {
            val dialog = LoginDialogFragment(this)
            dialog.show(childFragmentManager, "LoginDialogFragment")
        }

        binding.btnLogout.setOnClickListener {
            userViewModel.logout()
            binding.btnLogin.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE
            binding.txtWellcome.text = "Vui lòng Đăng Nhập!"
        }
    }

    override fun onUsernameReceived(username: String) {
        binding?.txtWellcome?.text = "Hello $username!"
    }
}

interface OnUsernameReceivedListener {
    fun onUsernameReceived(username: String)
}