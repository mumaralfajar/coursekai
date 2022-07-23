package com.mumaralfajar.coursekai.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mumaralfajar.coursekai.databinding.ActivityLoginBinding
import com.mumaralfajar.coursekai.presentation.forgotpassword.ForgotPasswordActivity
import com.mumaralfajar.coursekai.presentation.main.MainActivity
import com.mumaralfajar.coursekai.presentation.register.RegisterActivity
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        onAction()
    }

    private fun onAction() {
        loginBinding.apply {
            btnLogin.setOnClickListener {
                startActivity<MainActivity>()
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPasswordLogin.setOnClickListener {
                startActivity<ForgotPasswordActivity>()
            }
        }
    }
}