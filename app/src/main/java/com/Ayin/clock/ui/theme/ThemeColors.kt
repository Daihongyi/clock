package com.Ayin.clock.ui.theme

import androidx.compose.ui.graphics.Color

// 定义不同主题的颜色方案
data class LightColorScheme(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val surface: Color = Color.White,
    val onSurface: Color = Color.Black
)

object ThemeColors {
    val schemes = listOf(
        "Purple" to LightColorScheme(
            primary = Color(0xFF6750A4),
            secondary = Color(0xFFE8DEF8),
            tertiary = Color(0xFF7D5260),
            surface = Color(0xFFFFFBFE),
            onSurface = Color(0xFF1C1B1F)
        ),
        "Blue" to LightColorScheme(
            primary = Color(0xFF006C84),
            secondary = Color(0xFFB3E5FC),
            tertiary = Color(0xFF006A6A),
            surface = Color(0xFFF7F9FC),
            onSurface = Color(0xFF1A202C)
        ),
        "Green" to LightColorScheme(
            primary = Color(0xFF2E7D32),
            secondary = Color(0xFFC8E6C9),
            tertiary = Color(0xFF006C4F),
            surface = Color(0xFFF1F8E9),
            onSurface = Color(0xFF1B1B1B)
        ),
        "Orange" to LightColorScheme(
            primary = Color(0xFFE65100),
            secondary = Color(0xFFFFCC80),
            tertiary = Color(0xFF7B5E3F),
            surface = Color(0xFFFFF3E0),
            onSurface = Color(0xFF1C1B1B)
        ),
        "Red" to LightColorScheme(
            primary = Color(0xFFD32F2F),
            secondary = Color(0xFFFFCDD2),
            tertiary = Color(0xFFBA000D),
            surface = Color(0xFFFFF0F0),
            onSurface = Color(0xFF201A1A)
        ),
        "Teal" to LightColorScheme(
            primary = Color(0xFF00796B),
            secondary = Color(0xFFB2DFDB),
            tertiary = Color(0xFF004D40),
            surface = Color(0xFFF0FDF9),
            onSurface = Color(0xFF1A1C1A)
        ),
        "Indigo" to LightColorScheme(
            primary = Color(0xFF5D3FD3),
            secondary = Color(0xFFE8EAF6),
            tertiary = Color(0xFF303F9F),
            surface = Color(0xFFFAFAFC),
            onSurface = Color(0xFF1A1A1C)
        )
    )
}