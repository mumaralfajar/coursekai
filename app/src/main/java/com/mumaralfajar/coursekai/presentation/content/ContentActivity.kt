package com.mumaralfajar.coursekai.presentation.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.google.gson.Gson
import com.mumaralfajar.coursekai.adapter.PagesAdapter
import com.mumaralfajar.coursekai.databinding.ActivityContentBinding
import com.mumaralfajar.coursekai.model.Content
import com.mumaralfajar.coursekai.model.Material
import com.mumaralfajar.coursekai.model.Page
import com.mumaralfajar.coursekai.presentation.main.MainActivity
import com.mumaralfajar.coursekai.utils.*
import org.jetbrains.anko.startActivity

class ContentActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MATERIAL = "extra_material"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var contentBinding: ActivityContentBinding
    private lateinit var pagesAdapter: PagesAdapter
    private lateinit var contentDatabase: DatabaseReference
    private var currentPosition = 0
    private var materialPosition = 0

    private val listenerContent = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            if (snapshot.value != null) {
                showData()

                val json = Gson().toJson(snapshot.value)
                val content = Gson().fromJson(json, Content::class.java)

                pagesAdapter.pages = content?.pages as MutableList<Page>
            } else {
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
        }

    }

    private fun showEmptyData() {
        contentBinding.apply {
            ivEmptyDataContent.visible()
            vpContent.gone()
        }
    }

    private fun showData() {
        contentBinding.apply {
            ivEmptyDataContent.gone()
            vpContent.visible()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(contentBinding.root)

        pagesAdapter = PagesAdapter(this)
        contentDatabase = FirebaseDatabase.getInstance().getReference("contents")

        getDataIntent()
        onAction()
        viewPagerCurrentPosition()
    }

    private fun viewPagerCurrentPosition() {
        contentBinding.vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val totalIndex = pagesAdapter.count
                currentPosition = position
                val textIndex = "${currentPosition + 1} / $totalIndex"
                contentBinding.tvIndexContent.text = textIndex

                if (currentPosition == 0) {
                    contentBinding.btnPrevContent.invisible()
                    contentBinding.btnPrevContent.disabled()
                } else {
                    contentBinding.btnPrevContent.visible()
                    contentBinding.btnPrevContent.enabled()
                }
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}

        })
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
        contentDatabase
            .child(material.idMaterial.toString())
            .addValueEventListener(listenerContent)

        contentBinding.vpContent.adapter = pagesAdapter
        contentBinding.vpContent.setPagingEnabled(false)

        val textIndex = "${currentPosition + 1} / ${pagesAdapter.count}"
        contentBinding.tvIndexContent.text = textIndex
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
                if (currentPosition < pagesAdapter.count - 1) {
                    contentBinding.vpContent.currentItem += 1
                } else {
                    startActivity<MainActivity>(
                        MainActivity.EXTRA_MATERIAL to materialPosition + 1
                    )
                    finish()
                }
            }

            btnPrevContent.setOnClickListener {
                contentBinding.vpContent.currentItem -= 1
            }

            swipeContent.setOnRefreshListener { getDataIntent() }
        }
    }
}