package com.geo.cartwise.presentation.list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddItemBar(
    value: String,
    onValueChange: (String) -> Unit,
    priceValue: String,
    onPriceChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onScanClick: () -> Unit,
    onVoiceClick: () -> Unit,
    suggestions: List<String> = emptyList(),
    onSuggestionSelected: (String) -> Unit = {},
    onDismissSuggestions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Box lets the DropdownMenu anchor to the text field's position.
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Add an item, e.g. milk") },
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )
            ItemSuggestionDropdown(
                suggestions = suggestions,
                expanded = suggestions.isNotEmpty(),
                onSelect = onSuggestionSelected,
                onDismiss = onDismissSuggestions
            )
        }

        Spacer(modifier = Modifier.padding(top = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = priceValue,
                onValueChange = onPriceChange,
                modifier = Modifier.width(110.dp),
                placeholder = { Text("0.00") },
                label = { Text("Price") },
                prefix = { Text("₹") },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.weight(1f))
            FilledTonalIconButton(
                onClick = onScanClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Filled.CameraAlt, contentDescription = "Scan barcode")
            }
            Spacer(modifier = Modifier.width(6.dp))
            FilledTonalIconButton(
                onClick = onVoiceClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Filled.Mic, contentDescription = "Add items by voice")
            }
            Spacer(modifier = Modifier.width(6.dp))
            FilledIconButton(
                onClick = onSubmit,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add item")
            }
        }
    }
}
