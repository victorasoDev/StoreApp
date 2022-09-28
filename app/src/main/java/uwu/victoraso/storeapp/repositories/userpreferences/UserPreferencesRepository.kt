package uwu.victoraso.storeapp.repositories.userpreferences

import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.model.UserProfile
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : UserPreferencesRepositoryInterface {

    /** UserProfile loaded with necesary preferences **/
    override val getUserProfile: Flow<UserProfile> = preferencesDataSource.userDataStream

    /** UserName Preference **/
    override suspend fun setUserName(username: String) { preferencesDataSource.setName(username) }

    /** UserEmail Preference **/
    override suspend fun setUserEmail(email: String) { preferencesDataSource.setEmail(email) }
    override val getUserEmail: Flow<String> = preferencesDataSource.userEmail

    /** UserAdress Preference **/
    override suspend fun setUserAdress(adress: String) { preferencesDataSource.setAdress(adress) }

    /** UserPhone Preference **/
    override suspend fun setUserPhone(phone: String) { preferencesDataSource.setPhone(phone) }

    /** UserPassword Preference **/
    override suspend fun setUserPassword(password: String) { preferencesDataSource.setPassword(password) }
    override val getUserPassword: Flow<String> = preferencesDataSource.userPassword

    /** RememberMe Preference **/
    override suspend fun setRememberMe(rememberMe: Boolean) { preferencesDataSource.setRememberMe(rememberMe) }
    override val getRememberMe: Flow<Boolean> = preferencesDataSource.rememberMe

}

sealed interface UserPreferencesRepositoryInterface {
    /** User DataStore Preferences Getters **/
    val getUserProfile : Flow<UserProfile>
    val getUserEmail: Flow<String>
    val getUserPassword: Flow<String>
    val getRememberMe: Flow<Boolean>
    /** User DataStore Preferences Setters **/
    suspend fun setUserName(username: String)
    suspend fun setUserEmail(email: String)
    suspend fun setUserAdress(adress: String)
    suspend fun setUserPhone(phone: String)
    suspend fun setUserPassword(password: String)
    suspend fun setRememberMe(rememberMe: Boolean)
}