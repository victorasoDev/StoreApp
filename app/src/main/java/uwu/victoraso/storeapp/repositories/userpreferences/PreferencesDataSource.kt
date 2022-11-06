package uwu.victoraso.storeapp.repositories.userpreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import uwu.victoraso.storeapp.ds.*
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.ui.utils.CLEAR_USER_PREFERENCE
import javax.inject.Inject


class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) {

    val userDataStream = userPreferences.data
        .map {
            UserProfile(
                name = userName.firstOrNull() ?: "",
                email = userEmail.firstOrNull() ?: "",
                adress = userAdress.firstOrNull() ?: "",
                phone = userPhone.firstOrNull() ?: ""
            )
        }

    /** Setter & Getter of [USER_ADRESS_PREFERENCE] DataStore Preference **/
    private val userName: Flow<String> = userPreferences.data.map { preferences -> preferences[USER_NAME_PREFERENCE] ?: CLEAR_USER_PREFERENCE }

    suspend fun setName(name: String) = userPreferences.edit { preferences -> preferences[USER_NAME_PREFERENCE] = name }

    /** Setter & Getter of [USER_EMAIL_PREFERENCE] DataStore Preference **/
    val userEmail: Flow<String> = userPreferences.data.map { preferences -> preferences[USER_EMAIL_PREFERENCE] ?: CLEAR_USER_PREFERENCE }

    suspend fun setEmail(email: String) = userPreferences.edit { preferences -> preferences[USER_EMAIL_PREFERENCE] = email }

    /** Setter & Getter of [USER_NAME_PREFERENCE] DataStore Preference **/
    private val userAdress: Flow<String> = userPreferences.data.map { preferences -> preferences[USER_ADRESS_PREFERENCE] ?: CLEAR_USER_PREFERENCE }

    suspend fun setAdress(adress: String) = userPreferences.edit { preferences -> preferences[USER_ADRESS_PREFERENCE] = adress }

    /** Setter & Getter of [USER_PHONE_PREFERENCE] DataStore Preference **/
    private val userPhone: Flow<String> = userPreferences.data.map { preferences -> preferences[USER_PHONE_PREFERENCE] ?: CLEAR_USER_PREFERENCE }

    suspend fun setPhone(phone: String) = userPreferences.edit { preferences -> preferences[USER_PHONE_PREFERENCE] = phone }

    /** Setter & Getter of [USER_PASSWORD_PREFERENCE] DataStore Preference **/
    val userPassword: Flow<String> = userPreferences.data.map { preferences -> preferences[USER_PASSWORD_PREFERENCE] ?: CLEAR_USER_PREFERENCE }

    suspend fun setPassword(password: String) = userPreferences.edit { preferences -> preferences[USER_PASSWORD_PREFERENCE] = password }

    /** Setter & Getter of [REMEMBER_ME_PREFERENCE] DataStore Preference **/
    val rememberMe: Flow<Boolean> = userPreferences.data.map { preferences -> preferences[REMEMBER_ME_PREFERENCE] ?: false }

    suspend fun setRememberMe(rememberMe: Boolean) = userPreferences.edit { preferences -> preferences[REMEMBER_ME_PREFERENCE] = rememberMe }

    /** Setter & Getter of [DARK_MODE_PREFERENCE] DataStore Preference **/
    val darkThemeConfig: Flow<Boolean> = userPreferences.data.map { preferences -> preferences[DARK_MODE_PREFERENCE] ?: false }

    suspend fun setDarkThemeConfig(darkMode: Boolean) = userPreferences.edit { preferences -> preferences[DARK_MODE_PREFERENCE] = darkMode }

    /** Setter & Getter of [DARK_MODE_PREFERENCE] DataStore Preference **/
    val selectedCartIndex: Flow<Int> = userPreferences.data.map { preferences -> preferences[SELECTED_CART_INDEX_PREFERENCE] ?: 0 }

    suspend fun setSelectedCartIndex(selectedCartIndex: Int) = userPreferences.edit { preferences -> preferences[SELECTED_CART_INDEX_PREFERENCE] = selectedCartIndex }
}