package com.jacktan

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    companion object {
        private val BASE_URL = "http://build.jacktan.tech:5000/"
        private val JENKINS_USER = "testflight"
        private val JENKINS_PASSWD = "123456"
        private lateinit var BASE_AUTHOR: String
        private lateinit var apiService: ApiService
        fun getApiService(): ApiService {
            return apiService
        }
        init {
            apiService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient()
                    .newBuilder()
                    .addInterceptor(CommonInterceptor())
                    .build())
                .build()
                .create(ApiService::class.java)
            BASE_AUTHOR = Base64.encodeToString("$JENKINS_USER:$JENKINS_PASSWD".toByteArray(),
                Base64.NO_WRAP or Base64.URL_SAFE)
            print("base=$BASE_AUTHOR")
        }
    }


    class CommonInterceptor:  Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request()
                .newBuilder()
//                .addHeader("authorization","Basic dGVzdGZsaWdodDoxMjM0NTY=\n")
                .addHeader("authorization","Basic $BASE_AUTHOR")
                .build()
            return  chain.proceed(newRequest)
        }
    }
}