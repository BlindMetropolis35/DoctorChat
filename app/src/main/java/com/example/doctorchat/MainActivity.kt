package com.example.doctorchat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val yourUserID = findViewById<TextView>(R.id.your_user_id)
        val yourUserName = findViewById<TextView>(R.id.your_user_name)
        val userID = intent.getStringExtra("userID")
        val userName = intent.getStringExtra("userName")

        yourUserID.text = "Your User ID: $userID"
        yourUserName.text = "Your User Name: $userName"

        val appID: Long = 1098878319
        val appSign: String = "e21343c9c9de31efeb23fb5c1ad4947a68641cf24e1a37b9d9639ffeaa23a0c2"
        if (userID != null) {
            if (userName != null) {
                initCallInviteService(appID, appSign, userID, userName)
            }
        }

        initVoiceButton()
        initVideoButton()

        val userlogout = findViewById<TextView>(R.id.user_logout)
        userlogout.setOnClickListener {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Sign Out")
                .setMessage("Are you sure to Sign Out?")
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    ZegoUIKitPrebuiltCallInvitationService.unInit()
                    finish()
                }
                .create()
                .show()
        }
    }

    private fun initCallInviteService(
        appID: Long,
        appSign: String,
        userID: String,
        userName: String
    ) {
        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
        ZegoUIKitPrebuiltCallInvitationService.init(
            application, appID, appSign, userID, userName, callInvitationConfig
        )
    }

    private fun initVideoButton() {
        val newVideoCall = findViewById<ZegoSendCallInvitationButton>(R.id.new_video_call)
        newVideoCall.setIsVideoCall(true)
        newVideoCall.setOnClickListener(View.OnClickListener {
            val inputLayout = findViewById<TextInputLayout>(R.id.target_user_id)
            val targetUserID = inputLayout.editText?.text?.toString() ?: ""
            val users = targetUserID.split(",").map { userID ->
                val userName: String = userID + "_name"
                ZegoUIKitUser(userID, userName)
            }
            newVideoCall.setInvitees(users)
        })
    }


    private fun initVoiceButton() {
        val newVoiceCall = findViewById<ZegoSendCallInvitationButton>(R.id.new_voice_call)
        newVoiceCall.setIsVideoCall(false)
        newVoiceCall.setOnClickListener(View.OnClickListener {
            val inputLayout = findViewById<TextInputLayout>(R.id.target_user_id)
            val targetUserID = inputLayout.editText?.text?.toString() ?:""
            val users = targetUserID.split(",").map { userID ->
                val userName: String = userID + "_name"
                ZegoUIKitUser(userID, userName)
            }
            newVoiceCall.setInvitees(users)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        ZegoUIKitPrebuiltCallInvitationService.unInit()
    }
}