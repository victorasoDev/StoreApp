package uwu.victoraso.storeapp.repositories.userpreferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import uwu.victoraso.storeapp.ds.USER_ADRESS_PREFERENCE
import uwu.victoraso.storeapp.model.UserProfile
import uwu.victoraso.storeapp.ui.utils.DEBUG_TAG
import javax.inject.Inject


class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<Preferences>
) {

    val userDataStream = userPreferences.data
        .map {
            UserProfile(
                username = "victorasou",
                name = "Victor",
                adress = adressName.firstOrNull() ?: "No adress"
            )
        }

    private val adressName: Flow<String> = userPreferences.data.map { preferences ->
        Log.d(DEBUG_TAG, "saved -> ${preferences[USER_ADRESS_PREFERENCE]}")
        preferences[USER_ADRESS_PREFERENCE] ?: "Not adress provided"
    }

    suspend fun setAdress(adress: String) =
        userPreferences.edit { preferences ->
            preferences[USER_ADRESS_PREFERENCE] = adress
        }
}