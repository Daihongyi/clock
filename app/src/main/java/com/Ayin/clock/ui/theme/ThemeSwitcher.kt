package com.Ayin.clock.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ThemeSwitcher(
    currentThemeIndex: Int,
    onThemeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.Palette,
                contentDescription = "切换主题",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ThemeColors.schemes.forEachIndexed { index, (name, _) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            name,
                            color = if (index == currentThemeIndex) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    },
                    onClick = {
                        onThemeChange(index)
                        expanded = false
                    },
                    leadingIcon = {
                        if (index == currentThemeIndex) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "已选择",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}