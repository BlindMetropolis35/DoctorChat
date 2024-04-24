package com.example.doctorchat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputLayout
import com.tencent.mmkv.MMKV
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment
import java.util.Locale

class CallActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_call)

        MMKV.initialize(this)

        val userIDInput = findViewById<TextInputLayout>(R.id.user_id)
        val userNameInput = findViewById<TextInputLayout>(R.id.user_name)
        val userLogin = findViewById<Button>(R.id.user_login)

        userIDInput.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                userNameInput.editText!!.setText(
                    s.toString() + "_" + Build.MANUFACTURER.lowercase(
                        Locale.getDefault()
                    )
                )
            }
        })

        userIDInput.editText!!.setText("65e0249bda29bcc6974c3fd4")

        userLogin.setOnClickListener { v: View? ->
            val userID = userIDInput.editText!!.text.toString()
            val userName = userNameInput.editText!!.text.toString()
            signIn(userID, userName)
        }
    }

    private fun signIn(userID: String, userName: String) {
        if (TextUtils.isEmpty(userID) || TextUtils.isEmpty(userName)) {
            return
        }
        val progress = findViewById<CircularProgressIndicator>(R.id.progress_circular)
        progress.visibility = View.VISIBLE

        val fakeLoginProcess: Handler = Handler(Looper.getMainLooper())
        fakeLoginProcess.postDelayed(Runnable {
            progress.visibility = View.GONE
            // fake login success
            MMKV.defaultMMKV().putString("user_id", userID)
            MMKV.defaultMMKV().putString("user_name", userName)

            val intent = Intent(this@CallActivity, MainActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("userName", userName)
            startActivity(intent)
        }, 1000)
    }
}