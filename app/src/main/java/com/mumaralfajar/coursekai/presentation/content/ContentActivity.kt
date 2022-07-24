package com.mumaralfajar.coursekai.presentation.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.mumaralfajar.coursekai.adapter.PagesAdapter
import com.mumaralfajar.coursekai.databinding.ActivityContentBinding
import com.mumaralfajar.coursekai.model.Material
import com.mumaralfajar.coursekai.model.Page
import com.mumaralfajar.coursekai.repository.Repository
import org.jetbrains.anko.toast

class ContentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MATERIAL = "extra_material"
        const val EXTRA_POSITION = "extra_position"
    }
    private lateinit var contentBinding: ActivityContentBinding
    private lateinit var pagesAdapter: PagesAdapter
    private var currentPosition = 0
    private var materialPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(contentBinding.root)

        pagesAdapter = PagesAdapter(this)
        getDataIntent()
        onAction()
    }

    private fun getDataIntent() {
        if (intent != null) {
            materialPosition = intent.getIntExtra(EXTRA_POSITION, 0)
            val material = intent.getParcelableExtra<Material>(EXTRA_MATERIAL)

            contentBinding.tvTitleContent.text = material?.titleMaterial

            material?.let { getDataContent(material) }
        }
    }

    private fun getDataContent(material: Material) {
        showLoading()
        val content = material.idMaterial?.let { Repository.getContents(this)?.get(it) }

        Handler(Looper.getMainLooper())
            .postDelayed({
                hideLoading()

                pagesAdapter.pages = content?.pages as MutableList<Page>
                contentBinding.vpContent.adapter = pagesAdapter
                contentBinding.vpContent.setPagingEnabled(false)
            }, 1200)
    }

    private fun showLoading() {
        contentBinding.swipeContent.isRefreshing = true
    }

    private fun hideLoading() {
        contentBinding.swipeContent.isRefreshing = false
    }

    private fun onAction() {
        contentBinding.apply {
            btnCloseContent.setOnClickListener { finish() }

            btnNextContent.setOnClickListener {
                toast("Next")
            }

            btnPrevContent.setOnClickListener {
                toast("Prev")
            }

            swipeContent.setOnRefreshListener { getDataIntent() }
        }
    }
}