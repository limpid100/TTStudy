package com.dxl.ttstudy.ui.pinyin

import androidx.lifecycle.MutableLiveData
import com.dxl.ttstudy.base.BaseViewModel
import com.dxl.ttstudy.net.Api
import com.dxl.ttstudy.net.HttpClient
import com.dxl.ttstudy.net.successData
import com.dxl.ttstudy.ui.hanzi.Hanzi

class PinyinViewModel: BaseViewModel() {

    var currentPage = 1

    val dataListLiveData = MutableLiveData<List<Hanzi>>()

    fun request() {
        launch({
            dataListLiveData.value = HttpClient.getApiService<Api>().queryWords(currentPage, 1, 0).successData()
        })
    }
}