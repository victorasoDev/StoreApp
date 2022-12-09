package uwu.victoraso.storeapp.ui.utils

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank()
            && this.length >= MIN_PASS_LENGTH
            && Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.passwordMatches(repeated: String): Boolean {
    return this == repeated
}

fun String.checkCardName(): Boolean {
    val regex = "^[A-Z](?=.{1,29}$)[A-Za-z]*(?:\\h+[A-Z][A-Za-z]*)*$"
    return this.matches(regex.toRegex())
//    return pattern.matcher(this).matches()
}

fun String.checkCardNumber(): Boolean {
    val regex = "[0-9]{5}(?:[0-9]{3})?$"
    return this.matches(regex.toRegex())
}

fun String.checkCardDate(): Boolean {
    val regex = "(?:0[1-9]|1[0-2])/[0-9]{2}"
    return this.matches(regex.toRegex())
}

fun String.checkCardCVC(): Boolean {
    val regex = "[0-9]{3}"
    return this.matches(regex.toRegex())
}

fun String.formatDate(): String {
    return this.replace(" ", "").replace(",", "/")
}