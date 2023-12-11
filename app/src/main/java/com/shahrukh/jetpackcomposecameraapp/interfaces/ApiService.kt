package com.shahrukh.jetpackcomposecameraapp.interfaces

import com.shahrukh.jetpackcomposecameraapp.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {




    @Multipart
    @POST("API/core/upload?type=FILE")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part

    ): Response<UploadResponse>


}