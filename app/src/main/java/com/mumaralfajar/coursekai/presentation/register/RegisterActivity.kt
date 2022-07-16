package com.mumaralfajar.coursekai.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mumaralfajar.coursekai.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
    }
}