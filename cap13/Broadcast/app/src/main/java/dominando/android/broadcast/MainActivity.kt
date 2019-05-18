package dominando.android.broadcast

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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

    companion object {
        private const val ACTION_EVENT = "dominando.android.broadcast.ACTION_EVENT"
    }

}
