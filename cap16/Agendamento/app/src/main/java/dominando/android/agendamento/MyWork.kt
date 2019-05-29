package dominando.android.agendamento

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWork(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {

        val fisrtName = inputData.getString(PARAM_FIRST_NAME)
        val outputData = Data.Builder()
            .putString(PARAM_NAME, "$fisrtName Pereira")
            .putInt(PARAM_AGE, 32)
            .putLong(PARAM_TIME, System.currentTimeMillis())
            .build()

        return Result.success(outputData)
    }

    companion object {
        const val PARAM_FIRST_NAME = "fisrt_name"
        const val PARAM_NAME = "name"
        const val PARAM_AGE = "age"
        const val PARAM_TIME = "time"
    }

}