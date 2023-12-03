package com.shahrukh.jetpackcomposecameraapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahrukh.jetpackcomposecameraapp.repository.ImageRepository
import com.shahrukh.jetpackcomposecameraapp.response.UploadResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val _uploadResult = MutableStateFlow<UploadResponse?>(null)
    val uploadResult: StateFlow<UploadResponse?> get() = _uploadResult

    fun uploadImage(token: String,file: File) {
        viewModelScope.launch {
            repository.uploadImage(token,file).collect {
                _uploadResult.value = it
            }
        }
    }
}
