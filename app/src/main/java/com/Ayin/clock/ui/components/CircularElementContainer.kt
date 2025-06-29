package com.Ayin.clock.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularElementContainer(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(350.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
        content = content
    )
}