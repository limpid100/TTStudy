package com.dxl.ttstudy.net

import java.lang.RuntimeException


data class ApiResult<T>(val msg: String? = null, val code: Int, val data: T?) {
    val isSuccess: Boolean
        get() = code == 200
}

fun <T> ApiResult<T>.successData(): T {
    if (!isSuccess) throw ApiException(this)
    require(data != null) { "返回数据为空" }
    return data
}

class ApiException(val code: Int? = -1, val msg: String? = "", val status: Boolean = false) :
    RuntimeException() {
    constructor(result: ApiResult<*>) : this(
        code = result.code,
        msg = if (result.msg.isNullOrBlank()) "数据异常" else result.msg,
        status = false
    )

    override fun toString(): String {
        return "ApiException(code=$code, msg=$msg, status=$status)"
    }
}