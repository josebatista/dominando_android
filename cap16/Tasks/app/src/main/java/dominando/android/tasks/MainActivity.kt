package dominando.android.tasks

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

//    private val handler: MyHandler by lazy { MyHandler(this) }
//    private var thread: MyThread? = null

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler()

        btnStart.setOnClickListener {
            //            thread = MyThread(handler)
//            thread?.start()
            btnStart.isEnabled = false
            handler.post(MyThread(this))
        }

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
//        thread?.interrupt()
    }

    private class MyThread(activity: MainActivity) : Thread() {

        private val activityRef = WeakReference<MainActivity>(activity)
        private var count = 0

        override fun run() {
            super.run()
            activityRef.get()?.let { activity ->
                if (count < 10) {
                    count++
                    activity.txtMessage.text = "Contador: $count"
                    activity.handler.postDelayed(this, 1000)
                } else {
                    count = 0
                    activity.txtMessage.text = "Acabou!"
                    activity.btnStart.isEnabled = true
                }
            }
        }

    }

//    private class MyThread(handler: Handler) : Thread() {
//
//        private val handlerRef = WeakReference<Handler>(handler)
//
//        override fun run() {
//            super.run()
//            handlerRef.get()?.let { handler ->
//                for (i in 0 until 10) {
//                    val message = Message()
//                    message.what = MESSAGE_COUNT
//                    message.arg1 = i
//                    handler.sendMessage(message)
//                    try {
//                        sleep(1000)
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                }
//                handler.sendEmptyMessage(MESSAGE_FINISH)
//            }
//        }
//    }

//    private class MyHandler(activity: MainActivity) : Handler() {
//
//        val activityRef = WeakReference<MainActivity>(activity)
//
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            activityRef.get()?.let { activity ->
//                if (msg.what == MESSAGE_COUNT) {
//                    activity.txtMessage.text = "Contador: ${msg.arg1}"
//                } else if (msg.what == MESSAGE_FINISH) {
//                    activity.txtMessage.text = "Acabou!"
//                    activity.btnStart.isEnabled = true
//                }
//            }
//        }
//    }

    companion object {
        private const val MESSAGE_COUNT = 1
        private const val MESSAGE_FINISH = 2
    }

}
