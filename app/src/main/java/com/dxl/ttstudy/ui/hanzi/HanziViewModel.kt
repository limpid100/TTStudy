package com.dxl.ttstudy.ui.hanzi

import androidx.lifecycle.MutableLiveData
import com.dxl.ttstudy.base.BaseViewModel
import com.dxl.ttstudy.net.Api
import com.dxl.ttstudy.net.HttpClient
import com.dxl.ttstudy.net.successData

class HanziViewModel : BaseViewModel() {

    var currentPage = 1

    val hanziListLiveData = MutableLiveData<List<Hanzi>>()

    val hanziUpdateLiveData = MutableLiveData<Hanzi>()

    //0汉字  1拼音
    var type: Int = 0


    fun request() {
        launch({
            hanziListLiveData.value =
                HttpClient.getApiService<Api>().queryWords(currentPage, type, 1).successData()
        }, showLoadingDialog = currentPage == 1)
    }

    fun updateExercise(hanzi: Hanzi, isRight: Boolean) {
        launch({
            hanziUpdateLiveData.value =
                HttpClient.getApiService<Api>()
                    .updateExercise(hanzi.character, if (isRight) 1 else 0)
                    .successData()

        })
    }
}