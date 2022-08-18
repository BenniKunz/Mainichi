package bknz.example.mainichi.ui.coinScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mainichi.helper.db.AppDatabase
import com.example.mainichi.ui.cryptoScreen.AssetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val database: AppDatabase,
    private val assetRepository: AssetRepository
) :
    ViewModel() {

    private val handle: String? = savedStateHandle.get("coinID")

    private val _uiState: MutableStateFlow<CoinUiState> = MutableStateFlow(CoinUiState.LoadingState)
    val uiState: StateFlow<CoinUiState> = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<CoinEvent> = MutableSharedFlow()

    fun setEvent(event: CoinEvent) {
        viewModelScope.launch { _event.emit(event) }
    }

    private val _effect: Channel<CoinEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private fun setEffect(builder: () -> CoinEffect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    init {
        viewModelScope.launch {

//            val price = handle?.let { assetRepository.getPriceForAsset(it)

            val asset = assetRepository.getAsset(name = handle ?: "")

            _uiState.update {

                when (asset) {
                    null -> CoinUiState.ErrorState
                    else -> CoinUiState.ContentState(
                        asset = asset
                    )
                }
            }
        }
    }
}