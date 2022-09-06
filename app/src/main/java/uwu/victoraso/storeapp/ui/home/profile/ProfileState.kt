package uwu.victoraso.storeapp.ui.home.profile

data class ProfileState(
    val isLoading: Boolean = false,
    val adress: String = "No adress provided",
    val error: String? = ""
)