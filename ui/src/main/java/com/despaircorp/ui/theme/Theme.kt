package com.despaircorp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val primaryColorLight = Color(0xFF3B5452)
val secondaryColorLight = Color(0xFFBEA2AF)
val accentColorLight = Color(0xFF816A5A)
val textColorLight = Color(0xFF111817)
val backgroundColorLight = Color(0xFFEAF0F0)

val primaryColorDark = Color(0xFFABC4C2)
val secondaryColorDark = Color(0xFF5D414E)
val accentColorDark = Color(0xFFA58E7E)
val textColorDark = Color(0xFFE7EEED)
val backgroundColorDark = Color(0xFF0F1515)

private val DarkColorScheme = darkColorScheme(
    primary = primaryColorDark,
    secondary = secondaryColorDark,
    tertiary = accentColorDark,
    background = backgroundColorDark,
    outline = textColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = primaryColorLight,
    secondary = secondaryColorLight,
    tertiary = accentColorLight,
    background = backgroundColorLight,
    outline = textColorLight
    
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun RealEstateManagerKotlinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}