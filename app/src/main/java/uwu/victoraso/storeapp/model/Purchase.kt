package uwu.victoraso.storeapp.model

data class Purchase(
    val id: String = "",
    val userID: String = "",
    val userName: String = "",
    val userAdress: String = "",
    val userPhone: String = "",
    val cardDetails: CardDetails = CardDetails("", "", "", ""),
    val price: Long = 0,
    val date: String = "",
    val productsIDs: List<Long> = emptyList(),
)

data class PurchaseSimplified(
    val id: String,
    val userName: String,
    val userAdress: String,
    val price: Long,
    val date: String,
    val productsIDs: List<Long>
)

data class CardDetails(
    val cardName: String = "",
    val cardNumber: String = "",
    val cardExpireDate: String = "",
    val cardCVC: String = ""
)

fun CardDetails.checkCardDetails(): Boolean {
    return (cardName.isNotEmpty()
            && cardNumber.isNotEmpty()
            && cardExpireDate.isNotEmpty()
            && cardCVC.isNotEmpty())
}
