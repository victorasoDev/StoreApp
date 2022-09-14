package uwu.victoraso.storeapp.ui.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun formatPrice(price: Long): String {
    return NumberFormat.getCurrencyInstance().format(
        BigDecimal(price).movePointLeft(2)
    )
}