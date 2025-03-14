package io.rapidz.assignment1.ui.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import io.rapidz.assignment1.ui.*

@Preview
@Composable
fun BottomAppBarPreview() {
	DefaultTheme {
		BottomAppBar()
	}
}

@Composable
fun BottomAppBar(
	timer: String? = "",
	remainingTime: Int? = 0,
	onLeftDoubleArrowClick : (() -> Unit)? = null,
	onLeftArrowClick : (() -> Unit)? = null,
	onRightArrowClick : (() -> Unit)? = null,
	onRightDoubleArrowClick : (() -> Unit)? = null,
	onFloatingButtonClick : (() -> Unit)? = null,
	content: @Composable () -> Unit? = {}
) {
	Scaffold(
		bottomBar = {
			BottomAppBar(
				containerColor = if(remainingTime!! > 60) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.error,
				actions = {
					IconButton(onClick = { onLeftDoubleArrowClick!!()}) {
						Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = null)
					}
					IconButton(onClick = { onLeftArrowClick!!()}) {
						Icon(Icons.Default.ChevronLeft, contentDescription = null)
					}
					IconButton(onClick = { onRightArrowClick!!()}) {
						Icon(Icons.Default.ChevronRight, contentDescription = null)
					}
					IconButton(onClick = { onRightDoubleArrowClick!!()}) {
						Icon(Icons.Default.KeyboardDoubleArrowRight, null)
					}
					Row{
						Spacer(modifier = Modifier.width(spacing_20))
						Text(
							text = timer!!,
							style = MaterialTheme.typography.bodyMedium
						)
					}
				},
				floatingActionButton = {
					FloatingActionButton(
						containerColor = if (remainingTime!! < 60) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.primaryContainer,
						onClick = {
							if (onFloatingButtonClick != null) {
								onFloatingButtonClick()
							}
						},
						elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
					) {
						Icon(Icons.Default.DoneAll, null)
					}
				}
			)
		},
	) { innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		)
		content()
	}
}

@Preview
@Composable
fun BottomAppBarAdminPreview() {
	AdminTheme {
		BottomAppBarAdmin()
	}
}

@Composable
fun BottomAppBarAdmin(
	timer: String? = "",
	showDoneIcon: Boolean = true,
	showCloseIcon: Boolean = true,
	doneIconColor: Color = Color(0xFF018786),
	closeIcon: ImageVector = Icons.Default.Close,
	closeIconColor: Color = md_theme_admin_error,
	onDoneClick: () -> Unit = {},
	onCloseClick: () -> Unit = {},
	onLeftDoubleArrowClick : () -> Unit? = {},
	onLeftArrowClick : () -> Unit? = {},
	onRightArrowClick : () -> Unit? = {},
	onRightDoubleArrowClick : () -> Unit? = {},
	content: @Composable () -> Unit? = {}
) {
	Scaffold(
		bottomBar = {
			BottomAppBar(
				actions = {
					if (showDoneIcon) {
						IconButton(onClick = { onDoneClick() }) {
							Icon(Icons.Default.Done, contentDescription = null, tint = doneIconColor)
						}
					}
					if (showCloseIcon) {
						IconButton(onClick = { onCloseClick() }) {
							Icon(closeIcon, contentDescription = null, tint = closeIconColor)
						}
					}
					IconButton(onClick = { onLeftDoubleArrowClick()}) {
						Icon(Icons.Default.KeyboardDoubleArrowLeft, contentDescription = null)
					}
					IconButton(onClick = { onLeftArrowClick()}) {
						Icon(Icons.Default.ChevronLeft, contentDescription = null)
					}
					IconButton(onClick = { onRightArrowClick()}) {
						Icon(Icons.Default.ChevronRight, contentDescription = null)
					}
					IconButton(onClick = { onRightDoubleArrowClick()}) {
						Icon(Icons.Default.KeyboardDoubleArrowRight, null)
					}
					Row{
						Spacer(modifier = Modifier.width(spacing_20))
						Text(
							text = timer!!,
							style = MaterialTheme.typography.bodyLarge
						)

					}
				},
			)
		},
	) { innerPadding ->
		Box(
			modifier = Modifier
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
		)
		content()
	}
}
