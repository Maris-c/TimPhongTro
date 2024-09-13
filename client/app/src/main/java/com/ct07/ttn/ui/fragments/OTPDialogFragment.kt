package com.ct07.ttn.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.ct07.ttn.R
import com.ct07.ttn.models.User
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPDialogFragment(private val user: User) : DialogFragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationId: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_otp_dialog, null)

        auth = FirebaseAuth.getInstance()
        val btnVerify = view.findViewById<MaterialTextView>(R.id.btnVerifyOTP)
        val editOTP = view.findViewById<EditText>(R.id.otp)

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("OTPDialogFragment", "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("OTPDialogFragment", "onVerificationFailed", e)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("OTPDialogFragment", "onCodeSent:$verificationId")
                this@OTPDialogFragment.verificationId = verificationId
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+84" + user.phone_number, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            requireActivity(), // Activity (for callback binding)
            callbacks) // OnVerificationStateChangedCallbacks

        btnVerify.setOnClickListener {
            val otp = editOTP.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            signInWithPhoneAuthCredential(credential)
        }

        return builder.create()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("OTPDialogFragment", "signInWithCredential:success")
                    Toast.makeText(context, "OKE", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("OTPDialogFragment", "signInWithCredential:failure", task.exception)
                }
            }
    }
}