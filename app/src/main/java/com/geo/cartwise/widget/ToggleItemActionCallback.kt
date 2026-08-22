package com.geo.cartwise.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.geo.cartwise.CartWiseApp

// Key used to pass the item id through Glance's action parameters.
val ItemIdKey = ActionParameters.Key<Long>("item_id")

/**
 * Called when the user taps an item row in the widget.
 * Reads the current checked state from the DB, flips it, then forces a
 * widget redraw so the list refreshes without opening the app.
 *
 * Why ActionCallback instead of a BroadcastReceiver?
 * Glance's action system handles the lifecycle safely on a background
 * dispatcher — no need to manage threads or wake locks manually.
 */
class ToggleItemActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val itemId = parameters[ItemIdKey] ?: return
        val dao = (context.applicationContext as CartWiseApp).container.groceryItemDao

        // Read current state and toggle it.
        val item = dao.getById(itemId) ?: return
        dao.setChecked(itemId, !item.isChecked)

        // Tell every widget instance to redraw.
        CartWiseWidget().updateAll(context)
    }
}
