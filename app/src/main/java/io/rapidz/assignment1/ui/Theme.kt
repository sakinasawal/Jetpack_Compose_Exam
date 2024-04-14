package io.rapidz.assignment1.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.rapidz.assignment1.R

val AppFontFamily = FontFamily(
	Font(R.font.montserrat_light, FontWeight.Light),
	Font(R.font.montserrat_regular, FontWeight.Normal),
	Font(R.font.montserrat_medium, FontWeight.Medium),
	Font(R.font.montserrat_semibold, FontWeight.SemiBold)
)

// Material 3 typography
val AppTypography = Typography(
	headlineLarge = TextStyle(
		fontWeight = FontWeight.Normal,
		fontFamily = AppFontFamily,
		fontSize = 32.sp,
		lineHeight = 40.sp,
		letterSpacing = 0.sp
	),
	headlineMedium = TextStyle(
		fontWeight = FontWeight.Normal,
		fontFamily = AppFontFamily,
		fontSize = 28.sp,
		lineHeight = 36.sp,
		letterSpacing = 0.sp
	),
	headlineSmall = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 24.sp,
		lineHeight = 32.sp,
		letterSpacing = 0.sp
	),
	titleLarge = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.sp
	),
	titleMedium = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.15.sp
	),
	titleSmall = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp
	),
	bodyLarge = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.15.sp
	),
	bodyMedium = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.25.sp
	),
	bodySmall = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.4.sp
	),
	labelLarge = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 20.sp,
		letterSpacing = 0.1.sp
	),
	labelMedium = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp
	),
	labelSmall = TextStyle(
		fontFamily = AppFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 11.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp
	)
)

val DefaultColorScheme = lightColorScheme(
	primary = md_theme_default_primary,
	onPrimary = md_theme_default_onPrimary,
	primaryContainer = md_theme_default_primaryContainer,
	onPrimaryContainer = md_theme_default_onPrimaryContainer,
	secondary = md_theme_default_secondary,
	onSecondary = md_theme_default_onSecondary,
	secondaryContainer = md_theme_default_secondaryContainer,
	onSecondaryContainer = md_theme_default_onSecondaryContainer,
	tertiary = md_theme_default_tertiary,
	onTertiary = md_theme_default_onTertiary,
	tertiaryContainer = md_theme_default_tertiaryContainer,
	onTertiaryContainer = md_theme_default_onTertiaryContainer,
	error = md_theme_default_error,
	onError = md_theme_default_onError,
	errorContainer = md_theme_default_errorContainer,
	onErrorContainer = md_theme_default_onErrorContainer,
	outline = md_theme_default_outline,
	background = md_theme_default_background,
	onBackground = md_theme_default_onBackground,
	surface = md_theme_default_surface,
	onSurface = md_theme_default_onSurface,
	surfaceVariant = md_theme_default_surfaceVariant,
	onSurfaceVariant = md_theme_default_onSurfaceVariant,
	inverseSurface = md_theme_default_inverseSurface,
	inverseOnSurface = md_theme_default_inverseOnSurface,
	inversePrimary = md_theme_default_inversePrimary,
	surfaceTint = md_theme_default_surfaceTint,
	outlineVariant = md_theme_default_outlineVariant,
	scrim = md_theme_default_scrim,
)

private val AdminColorScheme = lightColorScheme(
	primary = md_theme_admin_primary,
	onPrimary = md_theme_admin_onPrimary,
	primaryContainer = md_theme_admin_primaryContainer,
	onPrimaryContainer = md_theme_admin_onPrimaryContainer,
	secondary = md_theme_admin_secondary,
	onSecondary = md_theme_admin_onSecondary,
	secondaryContainer = md_theme_admin_secondaryContainer,
	onSecondaryContainer = md_theme_admin_onSecondaryContainer,
	tertiary = md_theme_admin_tertiary,
	onTertiary = md_theme_admin_onTertiary,
	tertiaryContainer = md_theme_admin_tertiaryContainer,
	onTertiaryContainer = md_theme_admin_onTertiaryContainer,
	error = md_theme_admin_error,
	errorContainer = md_theme_admin_errorContainer,
	onError = md_theme_admin_onError,
	onErrorContainer = md_theme_admin_onErrorContainer,
	background = md_theme_admin_background,
	onBackground = md_theme_admin_onBackground,
	surface = md_theme_admin_surface,
	onSurface = md_theme_admin_onSurface,
	surfaceVariant = md_theme_admin_surfaceVariant,
	onSurfaceVariant = md_theme_admin_onSurfaceVariant,
	outline = md_theme_admin_outline,
	inverseOnSurface = md_theme_admin_inverseOnSurface,
	inverseSurface = md_theme_admin_inverseSurface,
	inversePrimary = md_theme_admin_inversePrimary,
	surfaceTint = md_theme_admin_surfaceTint,
	outlineVariant = md_theme_admin_outlineVariant,
	scrim = md_theme_admin_scrim,
)

@Composable
fun AdminTheme(content: @Composable () -> Unit) {
	MaterialTheme(
		colorScheme = AdminColorScheme,
		typography = AppTypography,
		content = content
	)
}

@Composable
fun DefaultTheme(content: @Composable () -> Unit) {
	MaterialTheme(
		colorScheme = DefaultColorScheme,
		typography = AppTypography,
		content = content
	)
}