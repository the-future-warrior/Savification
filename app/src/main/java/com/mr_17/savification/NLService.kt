package com.mr_17.savification

import android.app.Notification
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import com.mr_17.savification.room.Test
import com.mr_17.savification.room.TestingDatabase
import kotlinx.coroutines.*


class NLService : NotificationListenerService() {

    //private val testingDao = TestingDatabase.getInstance(this.applicationContext).testingDao
    /*private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)*/

    @RequiresApi(Build.VERSION_CODES.P)
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val mNotification: Notification = sbn!!.notification
        val extras: Bundle = mNotification.extras
        Log.d("notification", mNotification.toString())

        val intent = Intent(packageName)
        intent.putExtra(Constants.DB_ID, sbn.id)
        intent.putExtra(Constants.DB_PACKAGE_NAME, sbn.packageName)
        intent.putExtra(Constants.DB_TITLE, extras.getString(Notification.EXTRA_TITLE))
        intent.putExtra(Constants.DB_TEXT, extras.getString(Notification.EXTRA_TEXT).toString())
        intent.putExtra(Constants.DB_SUB_TEXT, extras.getString(Notification.EXTRA_SUB_TEXT))
        intent.putExtra(Constants.DB_CONTENT_TEXT, extras.getString(Notification.EXTRA_BIG_TEXT).toString())
        intent.putExtra(Constants.DB_SMALL_ICON, mNotification.smallIcon.resId)
        //intent.putExtra(Constants.DB_LARGE_ICON, mNotification.getLargeIcon()?.resId)
        intent.putExtra(Constants.DB_TIME_STAMP, sbn.postTime)
        sendBroadcast(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {

    }
}