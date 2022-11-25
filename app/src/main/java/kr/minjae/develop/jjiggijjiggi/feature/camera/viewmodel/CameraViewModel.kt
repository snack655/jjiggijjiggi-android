package kr.minjae.develop.jjiggijjiggi.feature.camera.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.minjae.develop.jjiggijjiggi.feature.camera.state.UploadPhotoState

class CameraViewModel : ViewModel() {

    private val _uploadPhotoState = MutableSharedFlow<UploadPhotoState>()
    val uploadPhotoState: SharedFlow<UploadPhotoState> = _uploadPhotoState

    fun uploadPhoto(storage: FirebaseStorage, uri: Uri) = viewModelScope.launch {

        _uploadPhotoState.emit(UploadPhotoState(isLoading = true))

        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child(FILE_STRING).child(fileName)
            .putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    storage.reference.child(FILE_STRING).child(fileName)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            viewModelScope.launch { _uploadPhotoState.emit(UploadPhotoState(url = uri.toString(), isLoading = false)) }
                        }.addOnFailureListener {
                            viewModelScope.launch { _uploadPhotoState.emit(UploadPhotoState(error = "사진 업로드에 실패했습니다.", isLoading = false)) }
                        }
                } else {
                   viewModelScope.launch { _uploadPhotoState.emit(UploadPhotoState(error = "사진 업로드에 실패했습니다.", isLoading = false)) }
                }
            }
    }

    companion object {
        private const val FILE_STRING = "jjiggi/photo"
    }

}