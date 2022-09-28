package uwu.victoraso.storeapp.repositories.userpreferences

import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.UserProfile
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : UserPreferencesRepositoryInterface {

    override val getUserProfile: Flow<UserProfile>
        get() = preferencesDataSource.userDataStream

    override suspend fun setUserName(username: String) { preferencesDataSource.setName(username) }

    override suspend fun setUserEmail(email: String) { preferencesDataSource.setEmail(email) }

    override val getUserEmail: Flow<String> = preferencesDataSource.userEmail

    override suspend fun setUserAdress(adress: String) { preferencesDataSource.setAdress(adress) }

    override suspend fun setUserPhone(phone: String) { preferencesDataSource.setPhone(phone) }
}

sealed interface UserPreferencesRepositoryInterface {
    val getUserProfile : Flow<UserProfile>
    val getUserEmail: Flow<String>
    /** User DataStore Preferences Setters **/
    suspend fun setUserName(username: String)
    suspend fun setUserEmail(email: String)
    suspend fun setUserAdress(adress: String)
    suspend fun setUserPhone(phone: String)
}