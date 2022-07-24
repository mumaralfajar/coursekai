package com.mumaralfajar.coursekai.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.mumaralfajar.coursekai.adapter.MaterialsAdapter
import com.mumaralfajar.coursekai.databinding.ActivityMainBinding
import com.mumaralfajar.coursekai.presentation.content.ContentActivity
import com.mumaralfajar.coursekai.presentation.user.UserActivity
import com.mumaralfajar.coursekai.repository.Repository
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MATERIAL = "extra_material"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var materialsAdapter: MaterialsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        materialsAdapter = MaterialsAdapter()

        getDataMaterial()
        onAction()
    }

    override fun onResume() {
        super.onResume()
        if (intent != null) {
            val position = intent.getIntExtra(EXTRA_POSITION, 0)
            mainBinding.rvMaterialsMain.smoothScrollToPosition(position)
        }
    }

    private fun getDataMaterial() {
        showLoading()
        val materials = Repository.getMaterials(this)

        Handler(Looper.getMainLooper())
            .postDelayed({
                hideLoading()
                materials?.let {
                    materialsAdapter.materials = it
                }
            }, 1200)

        mainBinding.rvMaterialsMain.adapter = materialsAdapter
    }

    private fun hideLoading() {
        mainBinding.swipeMain.isRefreshing = false
    }

    private fun showLoading() {
        mainBinding.swipeMain.isRefreshing = true
    }

    private fun onAction() {
        mainBinding.apply {
            ivAvatarMain.setOnClickListener {
                startActivity<UserActivity>()
            }

            etSearchMain.addTextChangedListener {
                materialsAdapter.filter.filter(it.toString())
            }

            etSearchMain.setOnEditorActionListener{_, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    val dataSearch = etSearchMain.text.toString().trim()
                    materialsAdapter.filter.filter(dataSearch)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            swipeMain.setOnRefreshListener {
                getDataMaterial()
            }
        }

        materialsAdapter.onClick { material, position ->
            startActivity<ContentActivity>(
                ContentActivity.EXTRA_MATERIAL to material,
                ContentActivity.EXTRA_POSITION to position
            )
        }
    }
}