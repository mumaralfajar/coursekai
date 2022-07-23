package com.mumaralfajar.coursekai.presentation.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mumaralfajar.coursekai.databinding.ActivityChangePasswordBinding
import org.jetbrains.anko.toast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var changePasswordBinding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changePasswordBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(changePasswordBinding.root)

        onAction()
    }

    private fun onAction() {
        changePasswordBinding.apply {
            btnChangePassword.setOnClickListener {
                toast("Change Password")
            }

            btnCloseChangePassword.setOnClickListener { finish() }
        }
    }
}