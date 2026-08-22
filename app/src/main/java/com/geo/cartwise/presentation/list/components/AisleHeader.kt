package com.geo.cartwise.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Sticky section header shown above each aisle group and above the
 * "Checked" section. Background fills the full row width so items
 * scroll underneath cleanly rather than bleeding through.
 */
@Composable
fun AisleHeader(
    label: String,
    modifier: Modifier = Modifier,
    muted: Boolean = false
) {
    Text(
        text = label.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = if (muted) {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
        } else {
            MaterialTheme.colorScheme.primary
        },
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}
