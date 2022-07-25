package com.mumaralfajar.coursekai.presentation.changepassword

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mumaralfajar.coursekai.R
import com.mumaralfajar.coursekai.databinding.ActivityChangePasswordBinding
import com.mumaralfajar.coursekai.utils.hideSoftKeyboard
import com.mumaralfajar.coursekai.utils.showDialogError
import com.mumaralfajar.coursekai.utils.showDialogLoading
import com.mumaralfajar.coursekai.utils.showDialogSuccess

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding
    private lateinit var dialogLoading: AlertDialog
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        currentUser = FirebaseAuth.getInstance().currentUser
        dialogLoading = showDialogLoading(this)

        onAction()
    }

    private fun onAction() {
        changePasswordBinding.apply {
            btnChangePassword.setOnClickListener {
                val oldPass = etOldPasswordChangePassword.text.toString().trim()
                val newPass = etNewPasswordChangePassword.text.toString().trim()
                val confirmNewPass = etConfirmNewPasswordChangePassword.text.toString().trim()

                if (checkValidation(oldPass, newPass, confirmNewPass)) {
                    hideSoftKeyboard(this@ChangePasswordActivity, changePasswordBinding.root)
                    changePasswordToServer(oldPass, newPass)
                }
            }

            btnCloseChangePassword.setOnClickListener { finish() }
        }
    }

    private fun changePasswordToServer(oldPass: String, newPass: String) {
        dialogLoading.show()
        currentUser?.let { mCurrentUser ->
            val credential = EmailAuthProvider.getCredential(mCurrentUser.email.toString(), oldPass)

            mCurrentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    mCurrentUser.updatePassword(newPass)
                        .addOnSuccessListener {
                            dialogLoading.dismiss()
                            val dialogSuccess = showDialogSuccess(this, getString(R.string.success_change_pass))
                            dialogSuccess.show()

                            Handler(Looper.getMainLooper())
                                .postDelayed({
                                    dialogSuccess.dismiss()
                                    finish()
                                }, 1500)
                        }
                        .addOnFailureListener {
                            dialogLoading.dismiss()
                            showDialogError(this, it.message.toString())
                        }
                }
                .addOnFailureListener {
                    dialogLoading.dismiss()
                    showDialogError(this, it.message.toString())
                }
        }
    }

    private fun checkValidation(oldPass: String, newPass: String, confirmNewPass: String): Boolean {
        changePasswordBinding.apply {
            when {
                oldPass.isEmpty() -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_password)
                    etOldPasswordChangePassword.requestFocus()
                }
                oldPass.length < 8 -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_input_your_old_password_more_than_8)
                    etOldPasswordChangePassword.requestFocus()
                }
                newPass.isEmpty() -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_new_password)
                    etOldPasswordChangePassword.requestFocus()
                }
                newPass.length < 8 -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_input_your_new_password_more_than_8)
                    etOldPasswordChangePassword.requestFocus()
                }
                confirmNewPass.isEmpty() -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_confirm_password)
                    etOldPasswordChangePassword.requestFocus()
                }
                confirmNewPass.length < 8 -> {
                    etOldPasswordChangePassword.error = getString(R.string.please_field_your_confirm_password_more_than_8)
                    etOldPasswordChangePassword.requestFocus()
                }
                newPass != confirmNewPass -> {
                    etNewPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etNewPasswordChangePassword.requestFocus()
                    etConfirmNewPasswordChangePassword.error = getString(R.string.your_password_didnt_match)
                    etConfirmNewPasswordChangePassword.requestFocus()
                }
                else -> return true
            }
        }

        return false
    }
}