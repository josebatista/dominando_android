package dominando.android.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtils {

    val CHANNEL_ID = "default"

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = context.getString(R.string.notif_channel_name)
        val channelDescription = context.getString(R.string.notif_channel_description)
        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = channelDescription
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun notificationSimples(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle(context.getString(R.string.notif_title))
            .setContentText(context.getString(R.string.notif_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.colorAccent))
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notificationBuilder.build())
    }

    fun notificationWithTapAction(context: Context) {}
    fun notificationBigText(context: Context) {}
    fun notificationWithButtonAction(context: Context) {}
    fun notificationAutoReply(context: Context) {}
    fun notificationInbox(context: Context) {}
    fun notificationHeadsUp(context: Context) {}

}