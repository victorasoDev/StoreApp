package uwu.victoraso.storeapp.model

import uwu.victoraso.storeapp.ui.utils.checkCardCVC
import uwu.victoraso.storeapp.ui.utils.checkCardDate
import uwu.victoraso.storeapp.ui.utils.checkCardName
import uwu.victoraso.storeapp.ui.utils.checkCardNumber

data class Purchase(
    val id: String = "",
    val userID: String = "",
    val userName: String = "",
    val userAdress: String = "",
    val userPhone: String = "",
    val cardDetails: CardDetails = CardDetails("", "", "", ""),
    val price: Long = 0,
    var date: String = "",
    val products: List<Product> = emptyList(),
)

data class CardDetails(
    val cardName: String = "",
    val cardNumber: String = "",
    val cardExpireDate: String = "",
    val cardCVC: String = ""
)

fun CardDetails.checkCardDetails(): Boolean {
    if (cardName.isNotEmpty() && !cardName.checkCardName()) return false
    if (cardNumber.isNotEmpty() && !cardNumber.checkCardNumber()) return false
    if (cardExpireDate.isNotEmpty() && !cardExpireDate.checkCardDate()) return false
    if (cardCVC.isNotEmpty() && !cardCVC.checkCardCVC()) return false
    return true
}
