package com.geo.cartwise.presentation.list.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Reusable text-entry row for adding a new item, plus a shortcut into barcode
 * scanning. Takes state + callbacks only — no ViewModel reference — so it can
 * be reused on any screen that needs "type a name, tap add".
 */
@Composable
fun AddItemBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Add an item, e.g. milk") },
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilledTonalIconButton(
            onClick = onScanClick,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(Icons.Filled.CameraAlt, contentDescription = "Scan barcode")
        }
        Spacer(modifier = Modifier.width(8.dp))
        FilledIconButton(
            onClick = onSubmit,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add item")
        }
    }
}
