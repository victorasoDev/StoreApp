package uwu.victoraso.storeapp.ds

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import uwu.victoraso.storeapp.model.Product
import uwu.victoraso.storeapp.repositories.Result
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : UserDataRepository {

    //TODO: se puede hacer para que te devuelva todos los datos en un flow, en Now In Android está

    override fun getUserAdress(): Flow<Result<String>> = flow {
        try {
            emit(Result.Loading())

            val userAdress = preferencesDataSource.adressName.first() //TODO first NO!

            emit(Result.Success(data = userAdress))
        } catch (e: Exception) {
            emit(Result.Error(message = e.localizedMessage ?: "Unknown error"))
        }
    }

    override suspend fun setUserAdress(adress: String) {
        preferencesDataSource.setAdress(adress)
    }

}