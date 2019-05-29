package dominando.android.agendamento

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val wm = WorkManager.getInstance()
    private var workId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnOneTime.setOnClickListener {
            val input = Data.Builder()
                .putString(MyWork.PARAM_FIRST_NAME, "José")
                .build()
            val request = OneTimeWorkRequest.Builder(MyWork::class.java)
                .setInputData(input)
                .build()
            observeAndEnqueue(request)
        }
        btnPeriodic.setOnClickListener {
            val input = Data.Builder().putString(MyWork.PARAM_FIRST_NAME, "José").build()
            val request = PeriodicWorkRequest.Builder(MyWork::class.java, 5, TimeUnit.MINUTES)
                .setInputData(input)
                .build()
            observeAndEnqueue(request)
        }
        btnStop.setOnClickListener {
            workId?.let { uuid ->
                wm.cancelWorkById(uuid)
            }
//            wm.cancelAllWork()
        }

//        callWorkers1()
        callWorkers2()

    }

    private fun observeAndEnqueue(request: WorkRequest) {
        wm.enqueue(request)
        workId = request.id
        wm.getWorkInfoByIdLiveData(request.id).observe(this, androidx.lifecycle.Observer { status ->
            txtStatus.text = when (status?.state) {
                WorkInfo.State.ENQUEUED -> "Enfileirado"
                WorkInfo.State.BLOCKED -> "Bloqueado"
                WorkInfo.State.CANCELLED -> "Cancelado"
                WorkInfo.State.RUNNING -> "Executando"
                WorkInfo.State.SUCCEEDED -> "Sucesso"
                WorkInfo.State.FAILED -> "Falhou"
                else -> "Indefinido"
            }
            txtOutput.text = status?.outputData?.run {
                """${getString(MyWork.PARAM_NAME)}
                    |${getInt(MyWork.PARAM_AGE, 0)}
                    |${getLong(MyWork.PARAM_TIME, 0)}""".trimMargin()
            }
        })
    }

    fun callWorkers1() {
        val request1 = OneTimeWorkRequest.Builder(Worker1::class.java).build()
        val request2 = OneTimeWorkRequest.Builder(Worker2::class.java).build()
        val request3 = OneTimeWorkRequest.Builder(Worker3::class.java).build()

        wm.beginWith(listOf(request1, request2))
            .then(request3)
            .enqueue()

    }

    fun callWorkers2() {
        val request0 = OneTimeWorkRequest.Builder(Worker1::class.java).build()
        val request1 = OneTimeWorkRequest.Builder(Worker1::class.java).build()
        val request2 = OneTimeWorkRequest.Builder(Worker2::class.java).build()
        val request3 = OneTimeWorkRequest.Builder(Worker3::class.java).build()

        wm.beginWith(request0)
            .then(listOf(request1, request2))
            .then(request3)
            .enqueue()

    }

    class Worker1(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
        override fun doWork(): Result {
            Thread.sleep(2000)
            val outputData = Data.Builder()
                .putString("title", "Dominnando Android")
                .build()

            return Result.success(outputData)
        }
    }

    class Worker2(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
        override fun doWork(): Result {
            Thread.sleep(1000)
            val outputData = Data.Builder()
                .putString("subtitle", "com Kotlin")
                .build()

            return Result.success(outputData)
        }
    }

    class Worker3(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
        override fun doWork(): Result {
            val input = inputData.run {
                getString("title") + " " + getString("subtitle")
            }

            Log.d("JPB", input)

            return Result.success()
        }
    }
}
