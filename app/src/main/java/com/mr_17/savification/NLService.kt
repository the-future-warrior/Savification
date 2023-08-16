package com.mr_17.savification

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.mr_17.savification.room.Test
import com.mr_17.savification.room.TestingDatabase
import kotlinx.coroutines.*


class NLService : NotificationListenerService() {

    //private val testingDao = TestingDatabase.getInstance(this.applicationContext).testingDao
    /*private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)*/

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val mNotification: Notification = sbn!!.notification
        val extras: Bundle = mNotification.extras
        Log.d("notification", extras.toString())

        /*scope.launch {
            testingDao.upsertTest(Test(data = extras.toString()))
        }*/
        /*GlobalScope.launch (Dispatchers.Main) {
            testingDao.upsertTest(Test(data = extras.toString()))
        }*/


        val intent = Intent(packageName)
        intent.putExtra("notif", extras.getString(Notification.EXTRA_TITLE))
        sendBroadcast(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {

    }
}