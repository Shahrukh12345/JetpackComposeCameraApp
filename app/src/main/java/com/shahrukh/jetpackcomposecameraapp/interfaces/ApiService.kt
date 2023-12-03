package com.shahrukh.jetpackcomposecameraapp.interfaces

import com.shahrukh.jetpackcomposecameraapp.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {




    @Multipart
    @POST("API/core/upload?type=FILE")
    suspend fun uploadImage(
        @HeaderMap token: String,
        @Part file: MultipartBody.Part

    ): Response<UploadResponse>


}