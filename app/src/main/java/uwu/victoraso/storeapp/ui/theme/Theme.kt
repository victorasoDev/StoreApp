package uwu.victoraso.storeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = StoreAppColors(
    brand = Shadow5,
    brandSecondary = Ocean3,
    uiBackground = Neutral0,
    buttonBackground = Lavender3,
    uiBorder = Neutral4,
    uiFloated = FunctionalGrey,
    textPrimary = Shadow7,
    textSecondary = Neutral7,
    textHelp = Neutral6,
    textInteractive = Neutral0,
    textLink = Ocean11,
    iconPrimary = Shadow3,
    iconSecondary = Neutral0,
    iconInteractive = Neutral0,
    iconInteractiveInactive = Neutral1,
    error = FunctionalRed,
    gradientDarkLavander = listOf(Lavender6, Lavender4, Lavender6),
    gradientLavander = listOf(Lavender4, Lavender3, Lavender4),
    tornado1 = listOf(Shadow4, Ocean3),
    isDark = false
)

private val DarkColorPalette = StoreAppColors(
    brand = Shadow1,
    brandSecondary = Ocean2,
    uiBackground = Neutral8,
    buttonBackground = Lavender4,
    uiBorder = Neutral3,
    uiFloated = FunctionalDarkGrey,
    textPrimary = Shadow1,
    textSecondary = Neutral0,
    textHelp = Neutral1,
    textInteractive = Neutral7,
    textLink = Ocean2,
    iconPrimary = Shadow1,
    iconSecondary = Neutral0,
    iconInteractive = Neutral7,
    iconInteractiveInactive = Neutral6,
    error = FunctionalRedDark,
    gradientDarkLavander = listOf(Lavender7, Lavender5, Lavender7),
    gradientLavander = listOf(Lavender5, Lavender4, Lavender5),
    tornado1 = listOf(Shadow4, Ocean3),
    isDark = true
)

object StoreAppTheme {
    val colors: StoreAppColors
        @Composable
        get() = LocalStoreAppColors.current
}

private val LocalStoreAppColors = staticCompositionLocalOf<StoreAppColors> {
    error("No StoreAppColorPalette provided")
}

@Stable
class StoreAppColors(
    gradientLavander: List<Color>,
    gradientDarkLavander: List<Color>,
    brand: Color,
    brandSecondary: Color,
    uiBackground: Color,
    buttonBackground: Color,
    uiBorder: Color,
    uiFloated: Color,
    interactiveSecondary: List<Color> = gradientLavander,
    textPrimary: Color,
    textSecondary: Color,
    textHelp: Color,
    textInteractive: Color,
    textLink: Color,
    tornado1: List<Color>,
    iconPrimary: Color,
    iconSecondary: Color,
    iconInteractive: Color,
    iconInteractiveInactive: Color,
    error: Color,
    isDark: Boolean
) {
    var gradientDarkLavander by mutableStateOf(gradientDarkLavander)
        private set
    var gradientLavander by mutableStateOf(gradientLavander)
        private set
    var brand by mutableStateOf(brand)
        private set
    var brandSecondary by mutableStateOf(brandSecondary)
        private set
    var uiBackground by mutableStateOf(uiBackground)
        private set
    var buttonBackground by mutableStateOf(buttonBackground)
        private set
    var uiBorder by mutableStateOf(uiBorder)
        private set
    var uiFloated by mutableStateOf(uiFloated)
        private set
    var interactiveSecondary by mutableStateOf(interactiveSecondary)
        private set
    var textPrimary by mutableStateOf(textPrimary)
        private set
    var textSecondary by mutableStateOf(textSecondary)
        private set
    var textHelp by mutableStateOf(textHelp)
        private set
    var textInteractive by mutableStateOf(textInteractive)
        private set
    var tornado1 by mutableStateOf(tornado1)
        private set
    var textLink by mutableStateOf(textLink)
        private set
    var iconPrimary by mutableStateOf(iconPrimary)
        private set
    var iconSecondary by mutableStateOf(iconSecondary)
        private set
    var iconInteractive by mutableStateOf(iconInteractive)
        private set
    var iconInteractiveInactive by mutableStateOf(iconInteractiveInactive)
        private set
    var error by mutableStateOf(error)
        private set
    var isDark by mutableStateOf(isDark)
        private set

    fun update(other: StoreAppColors) {
        gradientDarkLavander = other.gradientDarkLavander
        gradientLavander = other.gradientLavander
        brand = other.brand
        brandSecondary = other.brandSecondary
        uiBackground = other.uiBackground
        uiBorder = other.uiBorder
        uiFloated = other.uiFloated
        interactiveSecondary = other.interactiveSecondary
        textPrimary = other.textPrimary
        textSecondary = other.textSecondary
        textHelp = other.textHelp
        textInteractive = other.textInteractive
        textLink = other.textLink
        tornado1 = other.tornado1
        iconPrimary = other.iconPrimary
        iconSecondary = other.iconSecondary
        iconInteractive = other.iconInteractive
        iconInteractiveInactive = other.iconInteractiveInactive
        error = other.error
        isDark = other.isDark
    }

    fun copy(): StoreAppColors = StoreAppColors(
        gradientLavander = gradientLavander,
        gradientDarkLavander = gradientDarkLavander,
        brand = brand,
        brandSecondary = brandSecondary,
        uiBackground = uiBackground,
        buttonBackground = buttonBackground,
        uiBorder = uiBorder,
        uiFloated = uiFloated,
        interactiveSecondary = interactiveSecondary,
        textPrimary = textPrimary,
        textSecondary = textSecondary,
        textHelp = textHelp,
        textInteractive = textInteractive,
        textLink = textLink,
        tornado1 = tornado1,
        iconPrimary = iconPrimary,
        iconSecondary = iconSecondary,
        iconInteractive = iconInteractive,
        iconInteractiveInactive = iconInteractiveInactive,
        error = error,
        isDark = isDark,
    )
}

@Composable
fun StoreAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setSystemBarsColor(
            color = colors.uiBackground.copy(alpha = AlphaNearOpaque)
        )
    }
    ProvideStoreAppColors(colors) {
        MaterialTheme(
            colors = debugColors(darkTheme),
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Transparent

    @Composable
    override fun rippleAlpha() = RippleAlpha(0F, 0F, 0F, 0F)
}

@Composable
fun ProvideStoreAppColors(
    colors: StoreAppColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember {
        // Explicitly creating a new object here so we don't mutate the initial [colors]
        // provided, and overwrite the values set in it.
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalStoreAppColors provides colorPalette, content = content)
}

fun debugColors(
    darkTheme: Boolean,
    debugColor: Color = Color.Magenta
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)