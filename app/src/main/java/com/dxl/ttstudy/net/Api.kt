package com.dxl.ttstudy.net

import com.dxl.ttstudy.ui.hanzi.Hanzi
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    /**
     * [type] 0汉字  1拼音
     * [shuffle] 0不打乱  1打乱
     */
    @GET("word/queryWords")
    suspend fun queryWords(@Query("page")page:Int, @Query("type") type:Int, @Query("shuffle")shuffle:Int):ApiResult<List<Hanzi>>

    @GET("word/updateExercise")
    suspend fun updateExercise(@Query("character")character:String, @Query("isRight")isRight:Int):ApiResult<Hanzi>

}