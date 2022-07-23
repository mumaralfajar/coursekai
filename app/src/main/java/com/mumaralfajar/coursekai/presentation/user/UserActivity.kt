package com.mumaralfajar.coursekai.presentation.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import com.mumaralfajar.coursekai.databinding.ActivityUserBinding
import com.mumaralfajar.coursekai.presentation.changepassword.ChangePasswordActivity
import com.mumaralfajar.coursekai.presentation.login.LoginActivity
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    private lateinit var userBinding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        onAction()
    }

    private fun onAction() {
        userBinding.apply {
            btnCloseUser.setOnClickListener { finish() }

            btnChangeLanguageUser.setOnClickListener {
                startActivity(Intent(ACTION_LOCALE_SETTINGS))
            }

            btnChangePasswordUser.setOnClickListener {
                startActivity<ChangePasswordActivity>()
            }

            btnLogoutUser.setOnClickListener {
                startActivity<LoginActivity>()
                finishAffinity()
            }
        }
    }
}