package com.dxl.ttstudy.ui.hanzi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dxl.ttstudy.databinding.FragmentHanziBinding
import com.dxl.ttstudy.util.PinyinUtil.toPinyinVoiceFileName
import com.dxl.ttstudy.util.lllog

class HanziFragment : Fragment() {

    companion object {
        fun newInstance(hanzi: Hanzi): HanziFragment {
            return HanziFragment().apply {
                arguments = bundleOf("hanzi" to hanzi)
            }
        }
    }

    private val viewModel by viewModels<HanziViewModel>(this::requireActivity)

    private val soundUtils by lazy { (requireActivity() as HanziActivity).soundUtils }


    private lateinit var vb: FragmentHanziBinding

    private var hanzi: Hanzi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hanzi = arguments?.getParcelable("hanzi")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = FragmentHanziBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshView()
        vb.tvHanzi.setOnClickListener {
            soundUtils.read(hanzi)
        }
        viewModel.hanziUpdateLiveData.observe(viewLifecycleOwner) {
            if (it.id == hanzi?.id) {
                hanzi = it
                refreshView()
            }
        }

    }

    fun refreshView() {
        vb.tvHanzi.text = hanzi?.character
        vb.tvPinyin.text = hanzi?.pinyin
        vb.tvWords.text = hanzi?.words


    }


    override fun onResume() {
        super.onResume()
        refreshView()
    }


}