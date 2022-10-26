package com.dxl.ttstudy.hanzi

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.dxl.ttstudy.databinding.ActivityHanziBinding
import com.dxl.ttstudy.util.lllog

class HanziActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, HanziActivity::class.java))
    }

    private val viewModel by viewModels<HanziViewModel>()

    private lateinit var vb: ActivityHanziBinding

    private val pagerAdapter by lazy { ViewPagerAdapter(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityHanziBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.viewPager.adapter = pagerAdapter

        vb.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position >= pagerAdapter.hanziList.size - 5 && !viewModel.hanziListLiveData.value.isNullOrEmpty()) {
                    viewModel.request()
                }
                refreshBottom(position)
            }
        })

        listOf(vb.ivRight, vb.ivWrong).forEach {
            it.setOnClickListener { _ ->
                val index = vb.viewPager.currentItem
                val hanzi = pagerAdapter.hanziList.getOrNull(index) ?: return@setOnClickListener
                viewModel.updateExercise(hanzi, it.id == vb.ivRight.id)
                if (index < pagerAdapter.itemCount - 1) {
                    vb.viewPager.currentItem = index + 1
                }
            }
        }


        bindData()

        observe()
    }

    private fun refreshBottom(position: Int) {
        val hanzi = pagerAdapter.hanziList.getOrNull(position) ?: return
        val exerciseCount = hanzi.exerciseCount
        val rightCount = hanzi.rightCount
        val rightRate = if (exerciseCount <= 0) 0f else rightCount.toFloat() / exerciseCount
        vb.tvInfo.text = if (exerciseCount <= 0) "" else "做题次数$exerciseCount  正确$rightCount  正确率${(rightRate*100).toInt()}%"

        ValueAnimator.ofFloat(0f,vb.llProgress.width * rightRate).apply {
            setTarget(vb.viewProgress)
            duration = 1000
            addUpdateListener {
                vb.viewProgress.layoutParams = vb.viewProgress.layoutParams.apply {
                    width = (it.animatedValue as Float).toInt()
                }
            }
            start()
        }

    }

    private fun observe() {
        viewModel.hanziListLiveData.observe(this) { hanziList ->
            if (hanziList.isNotEmpty()) {
                pagerAdapter.hanziList.addAll(hanziList)
                pagerAdapter.notifyItemRangeInserted(
                    pagerAdapter.hanziList.size - hanziList.size,
                    hanziList.size
                )
            }
            viewModel.currentPage++
        }
    }


    private fun bindData() {
        viewModel.request()
    }


    class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        val hanziList = mutableListOf<Hanzi>()

        override fun getItemCount(): Int {
            return hanziList.size
        }

        override fun createFragment(position: Int): Fragment {
            return HanziFragment.newInstance(hanziList[position])
        }

    }


}