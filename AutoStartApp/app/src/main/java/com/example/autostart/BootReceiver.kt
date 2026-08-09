package com.example.autostart

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || 
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            
            val prefs = context.getSharedPreferences("AutoStartPrefs", Context.MODE_PRIVATE)
            val targetPackage = prefs.getString("target_package", null)
            
            if (targetPackage != null) {
                Log.d("BootReceiver", "Starting app: $targetPackage")
                val launchIntent = context.packageManager.getLaunchIntentForPackage(targetPackage)
                launchIntent?.let {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(it)
                }
            }
        }
    }
}
