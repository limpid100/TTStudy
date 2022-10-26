package com.dxl.ttstudy.net

import com.dxl.ttstudy.util.lllog
import com.dxl.ttstudy.util.llloge
import okhttp3.*

/**
 * @author dxl
 * @date 2022-03-31
 */
class LogInterceptor(block: (LogInterceptor.() -> Unit)? = null) : Interceptor {

    init {
        block?.invoke(this)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return kotlin.runCatching {
            chain.proceed(request)
        }.onFailure {
            llloge(it, "接口-请求")
        }.onSuccess {
            logRequest(request, chain.connection())
            logResponse(it)
        }.getOrThrow()
    }

    private fun logResponse(response: Response) {
        StringBuilder()
            .run {
                val peekBody = response.peekBody(1024 * 1024L)
                appendLine(peekBody.string())
                lllog(this.toString(), "接口-响应")
            }
    }

    /**
     * 打印请求
     */
    private fun logRequest(request: Request, connection: Connection?) {
        StringBuilder("\n").run {
            append("请求method : ${request.method} url: ${request.url} ")
            request.headers.forEach {
                if (it.first == "token") {
                    append("\n").append("请求header: ${it.first}=${it.second}")
                }
            }
            if (request.method == "POST") {
                val body = request.body

                if (body is FormBody) {
                    append("\n").append("POST params:")
                    for (i in 0 until body.size) {
                        append("\n").append(body.encodedName(i)).append("=")
                            .append(body.encodedValue(i))
                    }
                }
            }
            lllog(this.toString(), "接口-请求")
        }


    }
}