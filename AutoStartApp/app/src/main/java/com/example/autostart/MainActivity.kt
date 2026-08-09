package com.example.autostart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editTextPackageName)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnPermission = findViewById<Button>(R.id.btnPermission)

        val prefs = getSharedPreferences("AutoStartPrefs", Context.MODE_PRIVATE)
        editText.setText(prefs.getString("target_package", ""))

        btnSave.setOnClickListener {
            val packageName = editText.text.toString().trim()
            if (packageName.isNotEmpty()) {
                prefs.edit().putString("target_package", packageName).apply()
                Toast.makeText(this, "저장되었습니다: $packageName", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "패키지명을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        btnPermission.setOnClickListener {
            checkOverlayPermission()
        }
        
        // Initial permission check
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "다른 앱 위에 표시 권한이 필요합니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1234)
        } else {
            Toast.makeText(this, "이미 권한이 허용되어 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
