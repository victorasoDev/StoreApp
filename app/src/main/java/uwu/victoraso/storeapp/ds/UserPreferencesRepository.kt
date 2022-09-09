package uwu.victoraso.storeapp.ds

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.repositories.Result
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : UserPreferencesRepositoryInterface {

    override suspend fun setUserAdress(adress: String) {
        preferencesDataSource.setAdress(adress)
    }

    override val getUserProfile: Flow<UserProfile>
        get() = preferencesDataSource.userDataStream
}

sealed interface UserPreferencesRepositoryInterface {
    val getUserProfile : Flow<UserProfile> //TODO: hacer suspend esta función
    suspend fun setUserAdress(adress: String)
}