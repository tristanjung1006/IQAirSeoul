package com.example.airquality.retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 레트로핏 객체 생성
class RetrofitConnection {
    companion object {
        private const val BASE_URL = "https://api.airvisual.com/v2/"
        private var INSTANCE: Retrofit? = null

        fun getInstance() : Retrofit {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // 서버에서 온 json 응답을 data class로 자동 변환
                    .build()
            }

            return INSTANCE!!
        }
    }
}