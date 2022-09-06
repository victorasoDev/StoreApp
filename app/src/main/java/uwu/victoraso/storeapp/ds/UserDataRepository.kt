package uwu.victoraso.storeapp.ds

import kotlinx.coroutines.flow.Flow
import uwu.victoraso.storeapp.repositories.Result

interface UserDataRepository {

    fun getUserAdress() : Flow<Result<String>>
    suspend fun setUserAdress(adress: String)
}