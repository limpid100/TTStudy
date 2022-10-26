package com.dxl.ttstudy.util

import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import java.util.regex.Pattern

/**
 * 是否为数字
 */
fun String.isNumber(): Boolean {
    return Pattern.compile("^[-+]?[\\d]*$").matcher(this).matches()
}


fun String.toast() {
    ToastUtils.make()
        .setBgColor(Color.parseColor("#999999"))
        .setTextColor(Color.WHITE)
        .setDurationIsLong(true)
        .show(this)
}

fun String.toast(yOffset: Int) {
    ToastUtils.make()
        .setBgColor(Color.parseColor("#999999"))
        .setTextColor(Color.WHITE)
        .setGravity(Gravity.BOTTOM, 0, yOffset)
        .setDurationIsLong(true)
        .show(this)
}

fun String.toastCenter() {
    ToastUtils.make()
        .setBgColor(Color.parseColor("#999999"))
        .setTextColor(Color.WHITE)
        .setGravity(Gravity.CENTER, 0, 0)
        .setDurationIsLong(true)
        .show(this)
}

/**
 * 字符串获取非空值
 */
val String?.notNull: String
    get() = this ?: ""


/**
 * 是否包含，不连续
 * 比如abcdefg 包含 adf，但是不包含fda
 * @param str
 * @return
 */
fun String?.containsInContinuity(str: String?): Boolean {
    this?:return false
    str?.let {
        var index = 0
        for (c in this) {
            if (index >= str.length) break
            if (c == str[index]) index++
        }
        return index >= str.length
    }
    return false
}