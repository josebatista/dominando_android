package dominando.android.widgets

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import androidx.preference.PreferenceManager

class FavoritesReceiver : BroadcastReceiver() {

    private val sites = arrayOf(
        "www.nglauber.com.br",
        "developer.android.com",
        "www.google.com.br",
        "kotlinlang.org",
        "github.com/nglauber",
        "github.com/josebatista"
    )

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val action = intent.getStringExtra(FavoritesWidget.EXTRA_ACTION)
            if (action != null) {
                when (action) {
                    FavoritesWidget.ACTION_NEXT,
                    FavoritesWidget.ACTION_PREVIOUS -> {
                        val appWidgetId = intent.getIntExtra(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID
                        )

                        val position = newPosition(context, action, appWidgetId)
                        val views = RemoteViews(context.packageName, R.layout.widget_favorites)
                        views.setTextViewText(R.id.txtSite, sites[position])
                        val appWidgetManager = AppWidgetManager.getInstance(context)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                    FavoritesWidget.ACTION_DELETE -> {
                        val deletedWidgets = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
                        if (deletedWidgets != null) {
                            for (id in deletedWidgets) {
                                remove(context, id)
                            }
                        }
                    }
                    FavoritesWidget.ACTION_SITE -> {
                        val appWidgetId = intent.getIntExtra(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID
                        )
                        val position = getPosition(context, appWidgetId)
                        val site = sites[position]
                        val it = Intent(Intent.ACTION_VIEW, Uri.parse("http://$site"))
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(it)
                    }
                }
            }
        }
    }

    private fun newPosition(context: Context, action: String, appWidgetId: Int): Int {
        var position = getPosition(context, appWidgetId)

        if (FavoritesWidget.ACTION_NEXT == action) {
            position++
            if (position >= sites.size) {
                position = 0
            }
        } else if (FavoritesWidget.ACTION_PREVIOUS == action) {
            position--
            if (position < 0) {
                position = sites.size - 1
            }
        }

        savePosition(context, appWidgetId, position)

        return position
    }

    private fun getPosition(context: Context, appWidgetId: Int): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt("$PREF_KEY_PREFIX$appWidgetId", 0)
    }

    private fun savePosition(context: Context, appWidgetId: Int, position: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putInt("$PREF_KEY_PREFIX$appWidgetId", position).apply()
    }

    private fun remove(context: Context, appWidgetId: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().remove("$PREF_KEY_PREFIX$appWidgetId").apply()

    }

    companion object {
        private const val PREF_KEY_PREFIX = "widget_"
    }

}