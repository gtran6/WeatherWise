package com.myprojects.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.myprojects.repository.RepositoryInterface
import com.myprojects.weatherwise.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class AlertWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val repo by inject<RepositoryInterface>(RepositoryInterface::class.java)
    private val notificationHelper by inject<CreateNotification>(CreateNotification::class.java)
    private lateinit var body: String
    private lateinit var title: String

    override suspend fun doWork(): Result {

        val data = repo.getWeatherByLatLon(repo.getLatLon(), repo.getLanguage())
        if (data.alerts.isEmpty()) {
            body = "The weather is " + data.current.weather[0].description
            title = "There is no alert today"
        } else {
            body = "Pleas check the application"
            title = "Weather Alert"
        }
        withContext(Dispatchers.Main) {
            CreateNotification(context).createNotification(body, title)
            notificationHelper.createNotification(body, title)
        }
        return Result.success()
    }

}