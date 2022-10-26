package com.dxl.ttstudy.net

import com.dxl.ttstudy.hanzi.Hanzi
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("word/queryWords")
    suspend fun queryWords(@Query("page")page:Int):ApiResult<List<Hanzi>>

    @GET("word/updateExercise")
    suspend fun updateExercise(@Query("character")character:String, @Query("isRight")isRight:Int):ApiResult<Hanzi>

}