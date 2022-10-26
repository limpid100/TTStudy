package com.dxl.ttstudy.hanzi


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Hanzi(
    @SerializedName("character")
    val character: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("exercise_count")
    var exerciseCount: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("pinyin")
    val pinyin: String,
    @SerializedName("rate")
    var rate: Int,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("right_count")
    var rightCount: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("words")
    val words: String
):Parcelable