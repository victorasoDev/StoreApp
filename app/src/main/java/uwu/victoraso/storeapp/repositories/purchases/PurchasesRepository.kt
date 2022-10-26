package uwu.victoraso.storeapp.repositories.purchases

import uwu.victoraso.storeapp.model.Purchase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchasesRepository
@Inject
constructor(
    private val purchaseDataSource: PurchasesDataSource,
) : PurchasesRepositoryInterface{

    override fun makePurchase(purchase: Purchase): Boolean = purchaseDataSource.makePurchase(purchase)

//    override fun getPurchasesListByUser(userID: Long): Flow<List<Purchase>> {
//        TODO("Not yet implemented")
//    }

}

sealed interface PurchasesRepositoryInterface {
    fun makePurchase(purchase: Purchase): Boolean
//    fun getPurchasesListByUser(userID: Long): Flow<List<Purchase>>
}