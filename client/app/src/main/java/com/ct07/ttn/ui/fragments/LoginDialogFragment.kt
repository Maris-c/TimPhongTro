package com.ct07.ttn.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ct07.ttn.R
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.user.UserViewModel
import com.ct07.ttn.util.Resource
import com.google.android.material.textview.MaterialTextView

class LoginDialogFragment(private val listener: OnUsernameReceivedListener) : DialogFragment() {

    lateinit var userViewModel: UserViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity ?: throw IllegalStateException("Lỗi hoạt động!")

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_login_dialog, null)

        // Lấy dữ liệu tương ứng với các view
        val phoneNumber = view.findViewById<EditText>(R.id.loginPhoneNumber)
        val password = view.findViewById<EditText>(R.id.loginPassword)
        val btnLogin = view.findViewById<MaterialTextView>(R.id.btnLogin)
        val btnRegister = view.findViewById<MaterialTextView>(R.id.btnRegister)

        // Gọi API và lấy dữ liệu
        userViewModel = (activity as RoomActivity).userViewModel

        btnLogin.setOnClickListener {
            val phoneNumberText = phoneNumber.text.toString()
            val passwordText = password.text.toString()
            userViewModel.login(phoneNumberText, passwordText)
        }

        btnRegister.setOnClickListener {
            dismiss()
            val dialog = RegisterDialogFragment()
            dialog.show(parentFragmentManager, "RegisterDialogFragment") // Show the next dialog
        }

        userViewModel.users.observe(this, Observer { resource ->
            if (resource.status == Resource.Status.SUCCESS && resource.data?.isNotEmpty() == true) {
                listener.onUsernameReceived(resource.data?.get(0)?.username ?: "")
                Toast.makeText(requireContext(), "Đăng Nhập Thành Công!", Toast.LENGTH_LONG).show()
                dismiss()
            }
        })

        builder.setView(view)
        return builder.create()
    }
}