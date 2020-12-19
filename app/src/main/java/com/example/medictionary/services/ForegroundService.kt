package com.example.medictionary.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.medictionary.HomeActivity
import com.example.medictionary.R
import com.example.medictionary.snoozing.PlanarReceiver
import com.example.medictionary.snoozing.StopReceiver

class ForegroundService : Service() {
    val CHANNEL_ID = "pills"
    var mp : MediaPlayer? = null
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val element = intent!!.extras!!.getString("element")
        val idAudio = R.raw.ringtone
        val intent = Intent(baseContext, HomeActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(baseContext, 0, intent, 0)

        val intent_snooze = Intent(baseContext, PlanarReceiver::class.java).apply {
            putExtra("NOTIFICATION_NAME", element)
        }
        val pendingIntent_snooze: PendingIntent = PendingIntent.getBroadcast(baseContext, 0, intent_snooze, PendingIntent.FLAG_UPDATE_CURRENT)

        val intent_stop = Intent(baseContext, StopReceiver::class.java).apply {
            putExtra("NOTIFICATION_NAME", element)
        }
        val pendingIntent_stop: PendingIntent = PendingIntent.getBroadcast(baseContext, 0, intent_stop, PendingIntent.FLAG_UPDATE_CURRENT)

        Log.d("Mediia","Start")
        if (mp == null){
            mp = MediaPlayer.create(this, R.raw.ringtone)
            mp!!.isLooping = true
            mp!!.start()
        }

        val builder = NotificationCompat.Builder(baseContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle(element)
                .setContentText(baseContext.resources.getString(R.string.text_notification))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .addAction(R.drawable.ic_launcher_background,"snooze",pendingIntent_snooze)
                .addAction(R.drawable.ic_launcher_background,"stop",pendingIntent_stop)


        val notification  = builder.build()
        notification.flags = Notification.FLAG_INSISTENT or Notification.FLAG_ONGOING_EVENT

        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        try{
            mp!!.stop()
            mp!!.release()
            mp = null
        }catch (e:Exception){}
        super.onDestroy()
    }

}
