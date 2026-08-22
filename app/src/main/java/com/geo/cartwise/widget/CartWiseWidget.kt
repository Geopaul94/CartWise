package com.geo.cartwise.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.geo.cartwise.CartWiseApp
import com.geo.cartwise.data.local.entity.GroceryItemEntity

/**
 * Home-screen widget: shows unchecked items from the list with the most
 * remaining items. Tapping a row's checkmark toggles it without opening the app.
 *
 * [provideGlance] is a suspend function — Room queries here are safe because
 * Glance already dispatches on a background thread.
 */
class CartWiseWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val container = (context.applicationContext as CartWiseApp).container
        val itemDao = container.groceryItemDao
        val listDao = container.groceryListDao

        val busiest = itemDao.getListIdWithMostUnchecked()
        val listId = busiest?.listId
        val listName = if (listId != null) listDao.getNameById(listId) else null
        val uncheckedItems = if (listId != null) itemDao.getUncheckedByList(listId) else emptyList()

        provideContent {
            GlanceTheme {
                WidgetContent(
                    listName = listName ?: "CartWise",
                    items = uncheckedItems
                )
            }
        }
    }
}

@Composable
private fun WidgetContent(listName: String, items: List<GroceryItemEntity>) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(12.dp)
    ) {
        Text(
            text = listName,
            style = TextStyle(
                color = GlanceTheme.colors.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )

        if (items.isEmpty()) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "All done! ✓",
                    style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 13.sp)
                )
            }
        } else {
            LazyColumn(modifier = GlanceModifier.fillMaxWidth()) {
                items(items, itemId = { it.id }) { item ->
                    WidgetItemRow(item)
                }
            }
        }
    }
}

@Composable
private fun WidgetItemRow(item: GroceryItemEntity) {
    // The entire row is tappable — marks the item as checked.
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(
                actionRunCallback<ToggleItemActionCallback>(
                    actionParametersOf(ItemIdKey to item.id)
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "• ${item.name}",
            style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 13.sp),
            modifier = GlanceModifier.defaultWeight()
        )
        Text(
            text = "✓",
            style = TextStyle(color = GlanceTheme.colors.primary, fontSize = 14.sp),
            modifier = GlanceModifier.padding(start = 8.dp)
        )
    }
}
