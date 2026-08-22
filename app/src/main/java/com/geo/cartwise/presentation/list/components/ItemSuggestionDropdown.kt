package com.geo.cartwise.presentation.list.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A dropdown anchored to the name text field showing previously-added item
 * names that match what the user is typing.
 *
 * [expanded] controls visibility — pass `suggestions.isNotEmpty()`.
 * [onSelect] fills the name field and closes the dropdown.
 * [onDismiss] closes the dropdown without changing the field (e.g. tap outside).
 */
@Composable
fun ItemSuggestionDropdown(
    suggestions: List<String>,
    expanded: Boolean,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        suggestions.forEach { name ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = { onSelect(name) }
            )
        }
    }
}
