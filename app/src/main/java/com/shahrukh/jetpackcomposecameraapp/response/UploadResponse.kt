package com.shahrukh.jetpackcomposecameraapp.response

data class UploadResponse(
    val CipherMessageId: String,
    val downloadPath: String,
    val hash: String,
    val messageStatus: String,
    val name: String,
    val path: String,
    val responseCode: Int,
    val responseDescription: String,
    val timestamp: String,
    val type: String
)