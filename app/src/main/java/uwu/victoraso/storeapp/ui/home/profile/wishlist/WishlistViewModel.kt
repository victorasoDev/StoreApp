package uwu.victoraso.storeapp.ui.home.profile.wishlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.model.service.AccountService
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.asResult
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.repositories.wishlist.WishlistRepository
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
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
}

private fun wishlistStateStream(
    wishlistRepository: WishlistRepository,
    userId: String
): Flow<WishlistUiState> {
    val wishlistProductIds: Flow<List<Product>> = wishlistRepository.getUserWishlist(userId)

    return wishlistProductIds
        .asResult()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    WishlistUiState.Success(result.data)
                }
                is Result.Error -> {
                    Log.d(DEBUG_TAG, " --- Error")
                    WishlistUiState.Error
                }
                is Result.Loading -> {
                    Log.d(DEBUG_TAG, " --- Loading")
                    WishlistUiState.Loading
                }
            }
        }
}

sealed interface WishlistUiState {
    data class Success(val wishlist: List<Product>) : WishlistUiState
    object Error : WishlistUiState
    object Loading : WishlistUiState
}
