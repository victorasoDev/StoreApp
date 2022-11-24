package uwu.victoraso.storeapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Product(
    var id: Long = 0,
    var name: String = "",
    var imageUrl: String = "",
    var price: String = "",
    var tagline: String = "",
    var category: String = "",
    var isWishlist: Boolean = false
) : Parcelable

/**
 * Static data
 */

val products = listOf(
    Product(
        id = 1,
        name = "Red Dead Redemption 2",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1174180/header.jpg?t=1656615305",
        category = "Open-World",
        price = ""
    ),
    Product(
        id = 2,
        name = "Marvel’s Spider-Man Remastered",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1817070/header.jpg?t=1667406675",
        category = "Adventure",
        price = ""
    ),
    Product(
        id = 3,
        name = "DayZ",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/221100/header.jpg?t=1667310902",
        category = "Survival",
        price = ""
    ),
    Product(
        id = 4,
        name = "Rust",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/252490/header_alt_assets_19.jpg?t=1669127647",
        category = "Survival",
        price = ""
    ),
    Product(
        id = 5,
        name = "Raft",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/648800/header.jpg?t=1655744208",
        category = "Exploration",
        price = ""
    ),Product(
        id = 6,
        name = "Grounded",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/962130/header.jpg?t=1664324694",
        category = "Exploration",
        price = ""
    ),Product(
        id = 7,
        name = "The Forest",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/242760/header.jpg?t=1666811027",
        category = "Survival",
        price = ""
    ),Product(
        id = 8,
        name = "Days Gone",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1259420/header.jpg?t=1635476187",
        category = "Adventure",
        price = ""
    ),Product(
        id = 9,
        name = "God Of War",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1593500/header.jpg?t=1650554420",
        category = "Adventure",
        price = ""
    ),Product(
        id = 10,
        name = "",
        tagline = "A tag line",
        imageUrl = "",
        category = "",
        price = ""
    ),Product(
        id = 11,
        name = "UNCHARTED™: Legacy of Thieves Collection",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1659420/header.jpg?t=1666804146",
        category = "Adventure",
        price = ""
    ),Product(
        id = 12,
        name = "Kena: Bridge of Spirits",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1954200/header.jpg?t=1664298117",
        category = "Indie",
        price = ""
    ),Product(
        id = 13,
        name = "The Elder Scrolls V: Skyrim Special Edition",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/489830/header.jpg?t=1650909796",
        category = "Open-World",
        price = ""
    ),Product(
        id = 14,
        name = "Elden Ring",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1245620/header.jpg?t=1668042166",
        category = "Adventure",
        price = ""
    ),Product(
        id = 15,
        name = "Outer Wilds",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/753640/header.jpg?t=1660850445",
        category = "Exploration",
        price = ""
    ),Product(
        id = 16,
        name = "Subnautica",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/264710/header.jpg?t=1664411699",
        category = "Exploration",
        price = ""
    ),Product(
        id = 17,
        name = "Two Point Campus",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1649080/header.jpg?t=1667908870",
        category = "Simulation",
        price = ""
    ),Product(
        id = 18,
        name = "Cities Skylines",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/255710/header.jpg?t=1668007889",
        category = "Simulation",
        price = ""
    ),Product(
        id = 19,
        name = "Planet Zoo",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/703080/header.jpg?t=1668012490",
        category = "Simulation",
        price = ""
    ),Product(
        id = 20,
        name = "Going Medieval",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1029780/header_alt_assets_3.jpg?t=1667909578",
        category = "Simulation",
        price = ""
    ),Product(
        id = 21,
        name = "Kenshi",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/233860/header.jpg?t=1668010883",
        category = "Simulation",
        price = ""
    ),Product(
        id = 22,
        name = "Stray",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1332010/header.jpg?t=1660855681",
        category = "Adventure",
        price = ""
    ),Product(
        id = 23,
        name = "Resident Evil 7",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/418370/header.jpg?t=1656996016",
        category = "Horror",
        price = ""
    ),Product(
        id = 24,
        name = "Resident Evil 2",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/883710/header.jpg?t=1668647869",
        category = "Horror",
        price = ""
    ),Product(
        id = 25,
        name = "Scorn",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/698670/header.jpg?t=1667894197",
        category = "Horror",
        price = ""
    ),Product(
        id = 26,
        name = "Madison",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1670870/header.jpg?t=1661462096",
        category = "Horror",
        price = ""
    ),Product(
        id = 27,
        name = "The Ascent",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/979690/header.jpg?t=1661169382",
        category = "Multiplayer",
        price = ""
    ),Product(
        id = 28,
        name = "Borderlands 3",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/397540/header.jpg?t=1657214217",
        category = "Open-World",
        price = ""
    ),Product(
        id = 29,
        name = "PUBG: Battlegrounds",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/578080/header.jpg?t=1667814365",
        category = "Multiplayer",
        price = ""
    ),Product(
        id = 30,
        name = "Apex Legends",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1172470/header.jpg?t=1667588841",
        category = "Multiplayer",
        price = ""
    ),Product(
        id = 31,
        name = "Sea of Thieves",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1172620/header.jpg?t=1668434378",
        category = "Multiplayer",
        price = ""
    ),Product(
        id = 32,
        name = "Hades",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1145360/header.jpg?t=1661549369",
        category = "Rogue-Like",
        price = ""
    ),Product(
        id = 33,
        name = "The Binding of Isaac: Rebirth",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/250900/header.jpg?t=1617174663",
        category = "Rogue-Like",
        price = ""
    ),Product(
        id = 34,
        name = "Dead Cells",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/588650/header.jpg?t=1665137249",
        category = "Rogue-Like",
        price = ""
    ),Product(
        id = 35,
        name = "Vampire Survivors",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1794680/header.jpg?t=1666779006",
        category = "Rogue-Like",
        price = ""
    ),Product(
        id = 36,
        name = "Eastward",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/977880/header.jpg?t=1669027228",
        category = "Adventure",
        price = ""
    ),Product(
        id = 37,
        name = "Children of Morta",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/330020/header_alt_assets_1.jpg?t=1667827288",
        category = "Rogue-Like",
        price = ""
    ),Product(
        id = 38,
        name = "Risk of Rain 2",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/632360/header.jpg?t=1660063598",
        category = "Rogue-Like",
        price = ""
    ),Product(
        id = 39,
        name = "Portal 2",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/620/header.jpg?t=1665427328",
        category = "Multiplayer",
        price = ""
    ),Product(
        id = 40,
        name = "It Takes Two",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1426210/header.jpg?t=1666121755",
        category = "Multiplayer",
        price = ""
    ),Product(
        id = 41,
        name = "Litte Nightmares 2",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/860510/header.jpg?t=1661866261",
        category = "Horror",
        price = ""
    ),Product(
        id = 42,
        name = "Hollow Knight",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/367520/header.jpg?t=1667006028",
        category = "Metroidvania",
        price = ""
    ),Product(
        id = 43,
        name = "Blasphemous",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/774361/header.jpg?t=1660838704",
        category = "Metroidvania",
        price = ""
    ),Product(
        id = 44,
        name = "Forza Horizon 5",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1551360/header.jpg?t=1668017884",
        category = "Simulation",
        price = ""
    ),Product(
        id = 45,
        name = "Cult of the Lamb",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1313140/header.jpg?t=1667289686",
        category = "Metroidvania",
        price = ""
    ),Product(
        id = 46,
        name = "Ori and the Will of the Wisps",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1057090/header.jpg?t=1667504225",
        category = "Metroidvania",
        price = ""
    ),Product(
        id = 47,
        name = "Skul",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1147560/header.jpg?t=1656292935",
        category = "Metroidvania",
        price = ""
    ),Product(
        id = 48,
        name = "Slime Rancher 2",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1657630/header.jpg?t=1663866007",
        category = "Casual",
        price = ""
    ),Product(
        id = 49,
        name = "Stardew Valley",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/413150/header.jpg?t=1666917466",
        category = "Casual",
        price = ""
    ),Product(
        id = 50,
        name = "Overcooked 2",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/728880/header.jpg?t=1643298085",
        category = "Casual",
        price = ""
    ),Product(
        id = 51,
        name = "The Sims 4",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/1222670/header.jpg?t=1668103304",
        category = "Casual",
        price = ""
    ),Product(
        id = 52,
        name = "Return to Monkey Island",
        tagline = "A tag line",
        imageUrl = "https://cdn.cloudflare.steamstatic.com/steam/apps/2060130/header.jpg?t=1667933041",
        category = "Casual",
        price = ""
    ),

)