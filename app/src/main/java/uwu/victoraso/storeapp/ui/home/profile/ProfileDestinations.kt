package uwu.victoraso.storeapp.ui.home.profile

import uwu.victoraso.storeapp.MainDestinations

data class ProfileDestination(
    val destinationName: String,
    val route: String
)

val profileDestinationsList = listOf(
    ProfileDestination(
        destinationName = "Personal Info",
        route = MainDestinations.PERSONAL_INFO_ROUTE
    ),
    ProfileDestination(
        destinationName = "Wishlist",
        route = MainDestinations.WISHLIST_ROUTE
    ),
    ProfileDestination(
        destinationName = "Purchase History",
        route = MainDestinations.PURCHASE_HISTORY_ROUTE
    ),
    ProfileDestination(
        destinationName = "Settings",
        route = MainDestinations.SETTINGS_ROUTE
    )
)