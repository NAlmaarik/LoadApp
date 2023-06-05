package com.udacity

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityDetailBinding
import java.util.Objects

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()

        val fileName = intent.getStringExtra("FILE_Name").toString()
        val loadingStatus = intent.getStringExtra("LOADING_STATUS").toString()

        if(!fileName.isBlank())
            binding.contentDetail.fileValue.text = fileName

        if(!loadingStatus.isBlank()) {
            when(loadingStatus == getString(R.string.fail)){
                true -> {
                    binding.contentDetail.statusValue.setTextColor(Color.RED)
                    binding.contentDetail.statusValue.text = loadingStatus
                }
                else -> binding.contentDetail.statusValue.text = loadingStatus
            }

        }
        binding.contentDetail.okButton.setOnClickListener {
            onBackPressed()
        }
    }

}
