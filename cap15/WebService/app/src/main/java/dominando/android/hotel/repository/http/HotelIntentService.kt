package dominando.android.hotel.repository.http

import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.android.inject

class HotelIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val hotelHttp: HotelHttp by inject()
        val it = Intent(ACTION_SYNC)
        val lbm = LocalBroadcastManager.getInstance(this)

        try {

            hotelHttp.synchronizeWithServer()
            it.putExtra(EXTRA_SUCCESS, true)

        } catch (e: Exception) {
            it.putExtra(EXTRA_SUCCESS, false)
            e.printStackTrace()
        } finally {
            lbm.sendBroadcast(it)
        }

    }

    companion object {
        const val ACTION_SYNC = "sync_hotel"
        const val EXTRA_SUCCESS = "success"
    }
}