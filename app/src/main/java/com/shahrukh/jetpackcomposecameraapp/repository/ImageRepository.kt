package com.shahrukh.jetpackcomposecameraapp.repository

import com.shahrukh.jetpackcomposecameraapp.interfaces.ApiService
import com.shahrukh.jetpackcomposecameraapp.response.UploadResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun uploadImage(token: String,file: File): Flow<UploadResponse> {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        return flow {
            try {
                val response = apiService.uploadImage(token,body)
                response.body()?.let { emit(it) } // Assuming YourResponseModel contains the required data
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
