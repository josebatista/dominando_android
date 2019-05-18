package dominando.android.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class EventReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Toast.makeText(context, "Ação:\n$action", Toast.LENGTH_LONG).show()
    }
}