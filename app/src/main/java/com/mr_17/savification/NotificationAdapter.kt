package com.mr_17.savification

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mr_17.savification.room.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    private var list = mutableListOf<Test>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent,false)

        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = list[position]

        //holder.tvAppName.text = context.applicationContext.createPackageContext(notification.packageName, 0).applicationInfo.name
        holder.tvTitle.text = notification.title

        //context.applicationContext.packageManager.getApplicationInfo(notification.packageName!!, 0)
        val packageManager: PackageManager = context.applicationContext.packageManager
        val applicationInfo: ApplicationInfo? = try {
            packageManager.getApplicationInfo(notification.packageName!!, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        holder.tvAppName.text =
            (if (applicationInfo != null) packageManager.getApplicationLabel(applicationInfo) else "(unknown)") as String

        holder.tvAppName.text = notification.packageName

        val date = Date(notification.timeStamp!!)
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        holder.tvTimeStamp.text = dateFormat.format(date)
    }

    override fun getItemCount() = list.size

    fun setData(data: List<Test>) {
        list.apply {
            clear()
            addAll(data)
        }
    }
    fun setContext(context: Context) {
        this.context = context
    }

    class NotificationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvAppName: TextView = itemView.findViewById(R.id.tv_app_name)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvTimeStamp: TextView = itemView.findViewById(R.id.tv_time_stamp)

    }
}