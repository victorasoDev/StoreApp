package uwu.victoraso.storeapp.ui.home.profile.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.wishlist.WishlistRepository
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel
@Inject constructor(
    wishlistRepository: WishlistRepository,
    accountService: AccountService,
) : ViewModel() {

    var wishlistUiState: StateFlow<WishlistUiState> = wishlistStateStream(
        wishlistRepository = wishlistRepository,
        userId = accountService.getUserId(),
    )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WishlistUiState.Loading
        )

    private fun wishlistStateStream(
        wishlistRepository: WishlistRepository,
        userId: String
    ): Flow<WishlistUiState> {
        val wishlistProductIds: Flow<MutableList<Product>> = wishlistRepository.getUserWishlist(userId)

        return wishlistProductIds
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Success -> {
                        WishlistUiState.Success(result.data)
                    }
                    is Result.Loading -> {
                        WishlistUiState.Loading
                    }
                    is Result.Error -> {
                        WishlistUiState.Error
                    }
                }
            }.distinctUntilChanged()
    }
}

sealed interface WishlistUiState {
    data class Success(val wishlist: MutableList<Product>) : WishlistUiState
    object Error : WishlistUiState
    object Loading : WishlistUiState
}
