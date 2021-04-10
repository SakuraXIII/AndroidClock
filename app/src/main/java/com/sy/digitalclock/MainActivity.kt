package com.sy.digitalclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var num = 0
    private val LastDay = "2021年12月25日||00:00:00"

    private val runnable = object : Runnable {
        override fun run() {
            setClock()
            handler.postDelayed(this, 1000)
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == Intent.ACTION_DATE_CHANGED) {
                setClock()
            }
        }
    }
    private val handler = Handler()
    private var hasReceiver: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        if (!hasReceiver) {
            registerReceiver(receiver, IntentFilter(Intent.ACTION_DATE_CHANGED))
            hasReceiver = true
        }
    }

    override fun onResume() {
        super.onResume()
        hideAndFullscreen()
        setClock()
        findViewById<TextView>(R.id.last_days).text = LastDay.split("||")[0]
        handler.postDelayed(runnable, 1000)
    }


    private fun hideAndFullscreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }


    private fun setClock() {
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日||HH:mm:ss", Locale.CHINA)
        val date = Date(System.currentTimeMillis())
        val split = dateFormat.format(date).split("||")
        val last = dateFormat.parse(LastDay)
        val betweenDays = (last.time - date.time) / (1000 * 60 * 60 * 24)
        findViewById<TextView>(R.id.date).text = split[0]
        findViewById<TextView>(R.id.time).text = split[1]
        findViewById<TextView>(R.id.days).text = "${betweenDays + 1} 天"
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        hasReceiver = false
    }

    override fun onBackPressed() {
        if (num == 1) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "二次确认再退出", Toast.LENGTH_SHORT).show()
            num++
        }
    }


}