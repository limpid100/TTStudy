package com.dxl.ttstudy.hanzi

import androidx.lifecycle.MutableLiveData
import com.dxl.ttstudy.base.BaseViewModel
import com.dxl.ttstudy.net.Api
import com.dxl.ttstudy.net.HttpClient
import com.dxl.ttstudy.net.successData

class HanziViewModel : BaseViewModel() {

    var currentPage = 1

    val hanziListLiveData = MutableLiveData<List<Hanzi>>()

    val hanziUpdateLiveData = MutableLiveData<Hanzi>()



    fun request() {
        launch({
            hanziListLiveData.value =
                HttpClient.getApiService<Api>().queryWords(currentPage).successData()
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