package com.example.mainichi.helper.api.news

import retrofit2.http.GET

public interface NewsAPI {

    //f82c44be012e4c8895a986bfa3555eed 69dde205cbfe46c79fb75330f09bde07
    @GET("top-headlines?country=de&apiKey=f82c44be012e4c8895a986bfa3555eed")
    suspend fun getTopHeadlines() : TopHeadlines

}