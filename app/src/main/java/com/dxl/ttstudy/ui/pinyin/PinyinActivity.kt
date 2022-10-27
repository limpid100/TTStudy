package com.dxl.ttstudy.ui.pinyin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dxl.ttstudy.R
import com.dxl.ttstudy.databinding.ActivityPinyinBinding
import com.dxl.ttstudy.ui.hanzi.Hanzi

class PinyinActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, PinyinActivity::class.java))
    }

    private val viewModel by viewModels<PinyinViewModel>()

    private lateinit var vb: ActivityPinyinBinding

    private val adapter by lazy {
        object : BaseQuickAdapter<Hanzi, BaseViewHolder>(0), LoadMoreModule {

            override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                val textView = TextView(this@PinyinActivity).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    textSize = 38f
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        typeface = resources.getFont(R.font.oppo_sans)
                    }
                    setPadding(0, 50, 0, 50)
                    gravity = Gravity.CENTER
                    setTextColor(ContextCompat.getColor(this@PinyinActivity, R.color.black))
                }
                return createBaseViewHolder(textView)
            }

            override fun convert(holder: BaseViewHolder, item: Hanzi) {
                (holder.itemView as TextView).text = item.character
            }

        }.apply {
            loadMoreModule.setOnLoadMoreListener {
                viewModel.request()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityPinyinBinding.inflate(layoutInflater)
        setContentView(vb.root)
        vb.recyclerView.adapter = adapter
        viewModel.request()
        viewModel.dataListLiveData.observe(this) {
            if (viewModel.currentPage == 1) {
                adapter.setList(it)
            } else {
                adapter.addData(it)
            }
            viewModel.currentPage++
            if (it.isEmpty()) {
                adapter.loadMoreModule.loadMoreEnd(true)
            } else {
                adapter.loadMoreModule.loadMoreComplete()
            }
        }
    }


}