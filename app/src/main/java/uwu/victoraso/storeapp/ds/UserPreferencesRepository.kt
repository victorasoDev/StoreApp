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

    //TODO: se puede hacer para que te devuelva todos los datos en un flow, en Now In Android está

    override fun getUserAdress(): Flow<Result<String>> = flow {
        try {
            emit(Result.Loading())

            val userAdress = preferencesDataSource.adressName.firstOrNull() //TODO first NO!

            emit(Result.Success(data = userAdress))
        } catch (e: Exception) {
            emit(Result.Error(message = e.localizedMessage ?: "Unknown error"))
        }
    }

    override val getUserAdressNIA: Flow<String> = flow { emit(preferencesDataSource.adressName.first()) }

    override suspend fun setUserAdress(adress: String) {
        preferencesDataSource.setAdress(adress)
    }

    override val getUserProfile: Flow<UserProfile>
        get() = preferencesDataSource.userDataStream
}

sealed interface UserPreferencesRepositoryInterface {
    fun getUserAdress() : Flow<Result<String>> //TODO: hacer suspend esta función
    val getUserProfile : Flow<UserProfile> //TODO: hacer suspend esta función
    val getUserAdressNIA : Flow<String> //TODO: hacer suspend esta función
    suspend fun setUserAdress(adress: String)
}