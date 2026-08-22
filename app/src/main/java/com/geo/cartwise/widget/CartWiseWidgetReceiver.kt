package com.geo.cartwise.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * Android entry point for the widget. The system sends broadcasts here
 * (add/remove widget, scheduled updates). Glance routes them to [CartWiseWidget].
 *
 * Why is this so short?
 * GlanceAppWidgetReceiver handles all the boilerplate — we only need to point
 * it at our widget instance.
 */
class CartWiseWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CartWiseWidget()
}
