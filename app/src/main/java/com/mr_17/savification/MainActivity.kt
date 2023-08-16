package com.mr_17.savification

import android.content.*
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TestingDatabase::class.java,
            "testing.db"
        ).build()
    }

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

    public class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.d("received", intent.getStringExtra("notif")!!)
            GlobalScope.launch (Dispatchers.Main) {
                TestingDatabase.getInstance(context!!).testingDao.upsertTest(
                    Test(
                        data = intent.getStringExtra(
                            "notif"
                        )!!
                    )
                )
            }
        }
    }
}