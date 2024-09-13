package com.ct07.ttn.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.ct07.ttn.R
import com.ct07.ttn.models.User
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.user.UserViewModel
import com.ct07.ttn.util.Resource
import com.google.android.material.textview.MaterialTextView

class RegisterDialogFragment() : DialogFragment() {

    lateinit var userViewModel: UserViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity ?: throw IllegalStateException("Lỗi hoạt động!")

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_register_dialog, null)

        // Lấy dữ liệu tương ứng với các view
        val phoneNumber = view.findViewById<EditText>(R.id.registerUserPhone)
        val password = view.findViewById<EditText>(R.id.registerUserPassword)
        val repassword = view.findViewById<EditText>(R.id.registerUserRePassword)
        val username = view.findViewById<EditText>(R.id.registerUserName)
        val btnRegister = view.findViewById<MaterialTextView>(R.id.btnRegister)
        val btnLogin = view.findViewById<MaterialTextView>(R.id.btnLogin)

        // Gọi API và lấy dữ liệu
        userViewModel = (activity as RoomActivity).userViewModel

        btnRegister.setOnClickListener {
            val phoneNumberText = phoneNumber.text.toString()
            val passwordText = password.text.toString()
            val repasswordText = repassword.text.toString()
            val usernameText = username.text.toString()

            if (phoneNumberText.isEmpty() || passwordText.isEmpty() || repasswordText.isEmpty() || usernameText.isEmpty()) {
                Toast.makeText(activity, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phoneNumberText.length != 10) {
                Toast.makeText(activity, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                Toast.makeText(activity, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!usernameText.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                Toast.makeText(activity, "Tên người dùng không được chứa ký tự đặc biệt!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(username = usernameText,
                            phone_number = phoneNumberText,
                            password = passwordText,
                            id = 0,
                            favorite_room = 0,
                            level = "");

            if (passwordText != repasswordText) {
                Toast.makeText(activity, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (userViewModel.checkPhoneNumber(phoneNumberText)) {
                Toast.makeText(activity, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                userViewModel.register(user)
                Toast.makeText(activity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        btnLogin.setOnClickListener {
            dismiss() // Dismiss the current dialog
            val dialog = LoginDialogFragment(AccountFragment())
            dialog.show(parentFragmentManager, "LoginDialogFragment") // Show the next dialog
        }

        builder.setView(view)
        return builder.create()
    }
}