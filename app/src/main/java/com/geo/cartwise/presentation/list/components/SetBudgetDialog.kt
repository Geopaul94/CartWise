package com.geo.cartwise.presentation.list.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SetBudgetDialog(
    currentBudget: Double,
    onConfirm: (Double) -> Unit,
    onDismiss: () -> Unit
) {
    var input by remember {
        mutableStateOf(if (currentBudget > 0.0) "%.2f".format(currentBudget) else "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set List Budget") },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { raw ->
                    val filtered = raw.filter { it.isDigit() || it == '.' }
                        .let { s ->
                            val dot = s.indexOf('.')
                            if (dot == -1) s
                            else s.substring(0, dot + 1) + s.substring(dot + 1).replace(".", "")
                        }
                    input = filtered
                },
                label = { Text("Amount") },
                prefix = { Text("₹") },
                placeholder = { Text("0.00") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = input.toDoubleOrNull() ?: 0.0
                    onConfirm(amount)
                }
            ) { Text("Set") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
