package com.geo.cartwise.presentation.list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.geo.cartwise.domain.model.GroceryItem

/**
 * One row per item. Large tap target on the checkbox per the spec's
 * "large finger-friendly item rows" direction. Checked items fade via
 * animateColorAsState instead of an abrupt color swap.
 */
@Composable
fun GroceryItemRow(
    item: GroceryItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        targetValue = if (item.isChecked) {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "itemTextColor"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        )
        IconButton(onClick = onDelete) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Delete ${item.name}",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
