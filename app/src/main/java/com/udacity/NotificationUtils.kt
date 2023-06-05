package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody : String ,applicationContext: Context, fileName : String, loadingStatus : String){

    val contentIntent = Intent(applicationContext,DetailActivity::class.java)
    contentIntent.putExtra("FILE_Name", fileName)
    contentIntent.putExtra("LOADING_STATUS", loadingStatus)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        contentIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val loadingImage = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.ic_cloud)
    val largeIcon : Icon? = null

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(loadingImage)
        .bigLargeIcon(largeIcon)





    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.loading_notification_channel_id)
    )

        .setSmallIcon(R.drawable.ic_cloud_download)
        .setContentTitle(applicationContext
        .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setStyle(bigPicStyle)
        .setLargeIcon(loadingImage)
        .addAction(
            R.drawable.ic_cloud_download,
            applicationContext.getString(R.string.notification_button),
            contentPendingIntent
        )

    notify(NOTIFICATION_ID,builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}