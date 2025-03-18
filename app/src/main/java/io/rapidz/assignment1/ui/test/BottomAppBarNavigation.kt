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
import io.rapidz.assignment1.data.Role
import io.rapidz.assignment1.ui.*
import io.rapidz.assignment1.utils.Constants

@Preview
@Composable
fun BottomAppBarPreview() {
	DefaultTheme {
		BottomAppBar(role = Role(Constants.Role.ROLE_CANDIDATE))
	}
}

@Preview
@Composable
fun BottomAppBarAdminPreview() {
	AdminTheme {
		BottomAppBar(role = Role(Constants.Role.ROLE_ADMIN),
			showDoneIcon = true,
			showCloseIcon = true,
			showFloatBtn = false)
	}
}

@Composable
fun BottomAppBar(
	role : Role,
	showDoneIcon: Boolean? = false,
	showCloseIcon: Boolean? = false,
	doneIconColor: Color = Color(0xFF018786),
	closeIcon: ImageVector = Icons.Default.Close,
	closeIconColor: Color = md_theme_admin_error,
	onDoneClick: () -> Unit = {},
	onCloseClick: () -> Unit = {},
	onLeftDoubleArrowClick : () -> Unit? = {},
	onLeftArrowClick : () -> Unit? = {},
	onRightArrowClick : () -> Unit? = {},
	onRightDoubleArrowClick : () -> Unit? = {},
	showFloatBtn : Boolean? = true,
	onFloatingButtonClick : (() -> Unit)? = null,
	content: @Composable () -> Unit? = {}
) {
	Scaffold(
		bottomBar = {
			BottomAppBar(
				actions = {
					if (role.isAdmin()){
						if (showDoneIcon == true) {
							IconButton(onClick = { onDoneClick() }) {
								Icon(Icons.Default.Done, contentDescription = null, tint = doneIconColor)
							}
						}
						if (showCloseIcon == true) {
							IconButton(onClick = { onCloseClick() }) {
								Icon(closeIcon, contentDescription = null, tint = closeIconColor)
							}
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
							text = "00:00",
							style = if (role.isCandidate()){
								MaterialTheme.typography.bodyMedium
							} else {
								MaterialTheme.typography.bodyLarge
							}
						)
					}
				},
				floatingActionButton = {
					if (role.isCandidate() && showFloatBtn == true) {
						FloatingActionButton(
							onClick = { onFloatingButtonClick!!() },
							elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
						) {
							Icon(Icons.Default.DoneAll, null)
						}
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
