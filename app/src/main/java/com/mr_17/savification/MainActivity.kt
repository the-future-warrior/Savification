package com.mr_17.savification

import android.content.*
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.mr_17.savification.room.Test
import com.mr_17.savification.room.TestingDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private var enableNotificationListenerAlertDialog: AlertDialog? = null

    private lateinit var notificationReceiver: NotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // If the user did not turn the notification listener service on we prompt him to do so
        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog?.show();
        }

        // Finally we register a receiver to tell the MainActivity when a notification has been received
        // Finally we register a receiver to tell the MainActivity when a notification has been received
        notificationReceiver = NotificationReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(packageName)
        registerReceiver(notificationReceiver, intentFilter)
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch {
            val notificationList =
                TestingDatabase.getInstance(applicationContext).testingDao.getAllNotifications()
            val recyclerView = findViewById<RecyclerView>(R.id.rv_notification_list)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = NotificationAdapter().apply {
                    setData(notificationList)
                    setContext(context)
                }
            }
            /*for (notification in notificationList){
                Log.d("names123", createPackageContext(notification.packageName, CONTEXT_IGNORE_SECURITY).applicationInfo.name)
            }*/
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat: String = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun buildNotificationServiceAlertDialog(): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(getString(R.string.notification_listener_service))
        alertDialogBuilder.setMessage(getString(R.string.notification_listener_service_explanation))
        alertDialogBuilder.setPositiveButton(
            getString(R.string.yes)
        ) { _, _ -> startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        alertDialogBuilder.setNegativeButton(
            getString(R.string.no)
        ) { _, _ ->
            // If you choose to not enable the notification listener
            // the app will close as it will not work as expected
            finishAndRemoveTask()
        }
        return alertDialogBuilder.create()
    }

    class NotificationReceiver : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(context: Context?, intent: Intent) {
            //Log.d("received", intent.getStringExtra("title")!!)
            GlobalScope.launch (Dispatchers.Main) {
                TestingDatabase.getInstance(context!!).testingDao.upsertTest(
                    Test(
                        id = intent.getIntExtra(Constants.DB_ID, 0),
                        packageName = intent.getStringExtra(Constants.DB_PACKAGE_NAME),
                        title = intent.getStringExtra(Constants.DB_TITLE),
                        text = intent.getStringExtra(Constants.DB_TEXT),
                        subText = intent.getStringExtra(Constants.DB_SUB_TEXT),
                        contentText = intent.getStringExtra(Constants.DB_CONTENT_TEXT),
                        smallIcon = intent.getIntExtra(Constants.DB_SMALL_ICON, 0),
                        largeIcon = intent.getIntExtra(Constants.DB_LARGE_ICON, 0),
                        timeStamp = intent.getLongExtra(Constants.DB_TIME_STAMP, 0)
                    )
                )

            }
        }
    }
}