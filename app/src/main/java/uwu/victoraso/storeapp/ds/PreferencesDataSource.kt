package uwu.victoraso.storeapp.ds

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) {

    val adressName: Flow<String> = userPreferences.data.map { preferences ->
        preferences[USER_ADRESS_PREFERENCE] ?: "Not adress provided"
    }

    suspend fun setAdress(adress: String) =
        userPreferences.edit { preferences ->
            preferences[USER_ADRESS_PREFERENCE] = adress
        }
}