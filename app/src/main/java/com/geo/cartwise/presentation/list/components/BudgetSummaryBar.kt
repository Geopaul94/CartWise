package com.geo.cartwise.presentation.list.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val WARNING_THRESHOLD = 0.9f

/**
 * Compact summary strip shown at the top of a list when a budget is set.
 * Turns from primary to error color when spending reaches 90% of budget.
 */
@Composable
fun BudgetSummaryBar(
    totalEstimatedPrice: Double,
    budget: Double,
    modifier: Modifier = Modifier
) {
    val fraction = (totalEstimatedPrice / budget).coerceIn(0.0, 1.0).toFloat()
    val isNearLimit = fraction >= WARNING_THRESHOLD

    val trackColor by animateColorAsState(
        targetValue = if (isNearLimit) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary,
        animationSpec = tween(300),
        label = "budgetTrackColor"
    )
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(400),
        label = "budgetProgress"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₹${"%.2f".format(totalEstimatedPrice)} / ₹${"%.2f".format(budget)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isNearLimit) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (isNearLimit) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = "Near budget limit",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    Text(
                        text = "${(fraction * 100).toInt()}% used",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            LinearProgressIndicator(
                progress = { animatedFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                color = trackColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
