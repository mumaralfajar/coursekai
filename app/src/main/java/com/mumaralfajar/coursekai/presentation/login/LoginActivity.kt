package com.mumaralfajar.coursekai.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.mumaralfajar.coursekai.R
import com.mumaralfajar.coursekai.databinding.ActivityLoginBinding
import com.mumaralfajar.coursekai.presentation.forgotpassword.ForgotPasswordActivity
import com.mumaralfajar.coursekai.presentation.main.MainActivity
import com.mumaralfajar.coursekai.presentation.register.RegisterActivity
import com.mumaralfajar.coursekai.utils.hideSoftKeyboard
import com.mumaralfajar.coursekai.utils.showDialogError
import com.mumaralfajar.coursekai.utils.showDialogLoading
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        dialogLoading = showDialogLoading(this)

        onAction()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            startActivity<MainActivity>()
            finishAffinity()
        }
    }

    private fun onAction() {
        loginBinding.apply {
            btnLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val pass = etPasswordLogin.text.toString().trim()

                if (checkValidation(email, pass)) {
                    hideSoftKeyboard(this@LoginActivity, loginBinding.root)
                    loginToServer(email, pass)
                }
            }

            btnRegister.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btnForgotPasswordLogin.setOnClickListener {
                startActivity<ForgotPasswordActivity>()
            }
        }
    }

    private fun loginToServer(email: String, pass: String) {
        dialogLoading.show()
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                dialogLoading.dismiss()
                startActivity<MainActivity>()
                finishAffinity()
            }
            .addOnFailureListener {
                dialogLoading.dismiss()
                showDialogError(this, it.message.toString())
            }
    }

    private fun checkValidation(email: String, pass: String): Boolean {
        loginBinding.apply {
            when {
                email.isEmpty() -> {
                    etEmailLogin.error = getString(R.string.please_field_your_email)
                    etEmailLogin.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmailLogin.error = getString(R.string.please_use_valid_email)
                    etEmailLogin.requestFocus()
                }
                pass.isEmpty() -> {
                    etPasswordLogin.error = getString(R.string.please_field_your_password)
                    etPasswordLogin.requestFocus()
                }
                pass.length < 8 -> {
                    etPasswordLogin.error = getString(R.string.please_field_your_confirm_password_more_than_8)
                    etPasswordLogin.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }
}