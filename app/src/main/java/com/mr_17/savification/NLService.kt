package com.mr_17.savification

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


class NLService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val mNotification: Notification = sbn!!.notification
        val extras: Bundle = mNotification.extras
        Log.d("testing", extras.toString())
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {

    }
}