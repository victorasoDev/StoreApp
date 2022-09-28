package uwu.victoraso.storeapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    accountService: AccountService,
) : ViewModel() {

    var loginUiState: StateFlow<SplashUiState> = splashStateStream(
        accountService = accountService
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SplashUiState.Loading
        )
}

private fun splashStateStream(
    accountService: AccountService,
): Flow<SplashUiState> {
    val hasUser: Flow<Boolean> = flow { accountService.hasUser() }

    return hasUser
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    SplashUiState.Success(result.data)
                }
                is Result.Loading -> {
                    SplashUiState.Loading
                }
                is Result.Error -> {
                    SplashUiState.Error
                }
            }
        }
}

sealed interface SplashUiState {
    data class Success(val hasUser: Boolean) : SplashUiState
    object Error : SplashUiState
    object Loading : SplashUiState
}