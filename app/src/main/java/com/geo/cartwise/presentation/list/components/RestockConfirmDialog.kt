package com.geo.cartwise.presentation.list.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun RestockConfirmDialog(
    checkedCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Restock List?") },
        text = {
            Text(
                "This will uncheck all $checkedCount checked item${if (checkedCount == 1) "" else "s"}, " +
                "resetting the list for your next shopping trip."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Restock") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
