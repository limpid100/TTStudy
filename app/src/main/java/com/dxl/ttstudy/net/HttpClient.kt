package com.dxl.ttstudy.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.blankj.utilcode.util.AppUtils
import com.dxl.ttstudy.BuildConfig
import com.dxl.ttstudy.base.BaseApp
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

private fun getOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .callTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)   //不加上这句超时时间无效果
        .cache(Cache(File(BaseApp.instance.cacheDir, "request"), 1024 * 1024 * 100L))
        .addInterceptor(Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder: Request.Builder = originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("app", "android")
                .addHeader("version", AppUtils.getAppVersionName())

            requestBuilder.method(originalRequest.method, originalRequest.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        })
    if (BuildConfig.DEBUG) {
        builder.addNetworkInterceptor(LogInterceptor())
    }
    builder.addInterceptor { chain ->
        val request = chain.request()
        if (HttpClient.isNetworkAvailable(BaseApp.instance)) {
            /*
             *  If there is Internet, get the cache that was stored 5 seconds ago.
             *  If the cache is older than 5 seconds, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-age' attribute is responsible for this behavior.
             */
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        } else {
            /*
             *  If there is no Internet, get the cache that was stored 7 days ago.
             *  If the cache is older than 7 days, then discard it,
             *  and indicate an error in fetching the response.
             *  The 'max-stale' attribute is responsible for this behavior.
             *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
             */
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                .build()
        }

        chain.proceed(request)
    }
    return builder.build()

}

object HttpClient {

    /**
     * 判断网络是否连接
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    /**
     * 正式接口服务
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(UrlConst.API_BASE_URL)
            .build()
    }

    //
    inline fun <reified T> getApiService(): T {
        return retrofit.create(T::class.java)
    }

}



