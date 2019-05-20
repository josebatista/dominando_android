package dominando.android.broadcast

import android.content.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val receiver: InternalReceiver = InternalReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSend.setOnClickListener {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Toast.makeText(this, "BC Implicita", Toast.LENGTH_SHORT).show()
                Log.d("BC", "BC Implicita")
                sendImplicitBroadcastImp()
            } else {
                Toast.makeText(this, "BC Explicita", Toast.LENGTH_SHORT).show()
                Log.d("BC", "BC Explicita")
                sendImplicitBroadcastExp()
            }
        }

        btnLocal.setOnClickListener {
            val intent = Intent(ACTION_EVENT)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val filterLocal = IntentFilter(ACTION_EVENT)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filterLocal)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    /**
     * Para app com API 23 ou inferior
     */
    private fun sendImplicitBroadcastImp() {
        val intent = Intent(ACTION_EVENT)
        sendBroadcast(intent)
    }

    /**
     * Para API maiores que 23
     * intent implícita é convertida para intent explícita.
     */
    private fun sendImplicitBroadcastExp() {
        val intent = Intent(ACTION_EVENT)
        val matches = packageManager.queryBroadcastReceivers(intent, 0)
        for (resolveInfo in matches) {
            val explicit = Intent(intent)
            val componentName = ComponentName(
                resolveInfo.activityInfo.applicationInfo.packageName,
                resolveInfo.activityInfo.name
            )
            explicit.component = componentName
            sendBroadcast(explicit)
        }
    }

    inner class InternalReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            txtMessage.text = "Ação:\n${intent?.action}"
        }
    }

    companion object {
        private const val ACTION_EVENT = "dominando.android.broadcast.ACTION_EVENT"
    }

}
