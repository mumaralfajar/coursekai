package com.mumaralfajar.coursekai.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mumaralfajar.coursekai.databinding.ActivityMainBinding
import com.mumaralfajar.coursekai.presentation.user.UserActivity
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        onAction()
    }

    private fun onAction() {
        mainBinding.apply {
            ivAvatarMain.setOnClickListener {
                startActivity<UserActivity>()
            }
        }
    }
}