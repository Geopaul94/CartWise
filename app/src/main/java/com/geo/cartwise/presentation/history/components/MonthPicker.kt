package com.geo.cartwise.presentation.history.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.YearMonth
import java.time.format.DateTimeFormatter

// Display format: "Aug 2026"
private val displayFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

/**
 * A scrollable row of filter chips — one per available month.
 * Tapping a chip calls [onMonthSelected] with the "YYYY-MM" string.
 */
@Composable
fun MonthPicker(
    months: List<String>,
    selectedMonth: String,
    onMonthSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        months.forEach { month ->
            val label = runCatching {
                YearMonth.parse(month).format(displayFormatter)
            }.getOrDefault(month)

            FilterChip(
                selected = month == selectedMonth,
                onClick = { onMonthSelected(month) },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}
