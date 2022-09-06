package uwu.victoraso.storeapp.ui.home.profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uwu.victoraso.storeapp.ds.UserPreferencesRepository
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.Result
import uwu.victoraso.storeapp.repositories.products.ProductRepository
import uwu.victoraso.storeapp.ui.home.feed.ProductListState
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel()
{

    private val _state: MutableState<ProfileState> = mutableStateOf(ProfileState())
    val state: State<ProfileState> = _state

    init {
        getAdress()
        Log.d(DEBUG_TAG, state.value.toString())
    }

    fun setAdress(adress: String) {
        viewModelScope.launch {
            userPreferencesRepository.setUserAdress(adress)
        }
    }

    fun getAdress() {
        userPreferencesRepository.getUserAdress().onEach { result ->
            when(result) {
                is Result.Error -> {
                    _state.value = ProfileState(error = result.message ?: "Unknown error")
                }
                is Result.Loading -> {
                    _state.value = ProfileState(isLoading = true)
                }
                is Result.Success -> {
                    _state.value = ProfileState(adress = result.data.toString())
                }
            }
        }.launchIn(viewModelScope)
    }
}