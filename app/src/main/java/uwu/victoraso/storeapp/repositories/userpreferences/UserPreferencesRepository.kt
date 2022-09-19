package uwu.victoraso.storeapp.repositories.userpreferences

import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.UserProfile
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
    val getUserProfile : Flow<UserProfile>
    suspend fun setUserAdress(adress: String)
}