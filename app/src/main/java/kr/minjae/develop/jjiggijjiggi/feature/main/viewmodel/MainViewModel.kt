package kr.minjae.develop.jjiggijjiggi.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.minjae.develop.jjiggijjiggi.feature.main.state.OcrState
import kr.minjae.develop.jjiggijjiggi.network.repository.ocr.OcrRepository
import kr.minjae.develop.jjiggijjiggi.network.request.ImageRequest
import kr.minjae.develop.jjiggijjiggi.network.request.OcrRequest

@HiltViewModel
class MainViewModel @Inject constructor(
    private val ocrRepository: OcrRepository
) : ViewModel() {
    
    private val _ocrState = MutableStateFlow<OcrState>(OcrState())
    val ocrState: StateFlow<OcrState> = _ocrState

    fun getTextInImage() {
        _ocrState.value = OcrState(isLoading = true)
        viewModelScope.launch {
            ocrRepository.getTextInImage(
                OcrRequest(
                    images = listOf(ImageRequest(
                        format = "jpg",
                        name = "medium",
                        url = "http://www.dsbachi.com/web/product/medium/202203/44b9eefbcad52c710d8e0b9ab8a75344.jpg")
                    )
                )
            )
                .onSuccess { _ocrState.value = OcrState(isLoading = false, ocrData = it) }
                .onFailure { _ocrState.value = OcrState(isLoading = false, error = "텍스트를 받아오지 못했습니다.") }

        }
    }
}