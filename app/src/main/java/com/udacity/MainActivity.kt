package com.udacity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var selectedFile : String = ""
    private var loadingStatus : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        createChannel(
            getString(R.string.loading_notification_channel_id) ,
            getString(R.string.loading_notification_channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentMainLayout.customButton.setOnClickListener {
            when (binding.contentMainLayout.radioGroup.checkedRadioButtonId) {
                R.id.first_file -> {
                    selectedFile = getString(R.string.first_file)
                    binding.contentMainLayout.customButton.buttonState = ButtonState.Loading
                    notificationManager.cancelAll()
                    download(FIRST_URL) }
                R.id.second_file -> {
                    selectedFile = getString(R.string.second_file)
                    binding.contentMainLayout.customButton.buttonState = ButtonState.Loading
                    notificationManager.cancelAll()
                    download(SECOND_URL) }
                R.id.third_file -> {
                    selectedFile = getString(R.string.third_file)
                    binding.contentMainLayout.customButton.buttonState = ButtonState.Loading
                    notificationManager.cancelAll()
                    download(THIRD_URL) }
                else -> Toast.makeText(
                    this,
                    resources.getString(R.string.no_selection),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val query = DownloadManager.Query()
            id?.let {
                query.setFilterById(it)
            }
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(query)
            if(cursor.moveToFirst()){
                if(cursor.count > 0) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    when(status){
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            binding.contentMainLayout.customButton.buttonState = ButtonState.Completed
                            notificationManager.sendNotification(resources.getString(R.string.app_name), applicationContext,selectedFile,getString(R.string.success))
                        }
                        DownloadManager.STATUS_FAILED ->{
                            binding.contentMainLayout.customButton.buttonState = ButtonState.Completed
                            notificationManager.sendNotification(resources.getString(R.string.app_name), applicationContext,selectedFile,getString(R.string.fail))
                        }
                    }
                }
            }


        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Loading Completed"

            val  notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val FIRST_URL ="https://github.com/bumptech/glide"
//            "https://github.com/bumptech/glide/archive/master.zip"
        private const val SECOND_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
//            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val THIRD_URL = "https://github.com/square/retrofit"
//            "https://github.com/square/retrofit/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
}