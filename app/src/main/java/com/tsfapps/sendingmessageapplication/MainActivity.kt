package com.tsfapps.sendingmessageapplication

import android.Manifest
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tsfapps.sendingmessageapplication.MainActivity.Constant.PHONEPE_PACKAGE_NAME


class MainActivity : AppCompatActivity() {

    // on below line we are creating variable
    // for edit text phone and message and button
    lateinit var phoneEdt: EditText
    lateinit var messageEdt: EditText
    lateinit var sendMsgBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("TSF_APPS", "PhonePe Exist: ${doesPhonePeExist()}")
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestMultiplePermissions.launch(arrayOf(Manifest.permission.SEND_SMS))
        }
        // initializing variables of phone edt,
        // message edt and send message btn.
        phoneEdt = findViewById(R.id.idEdtPhone)
        messageEdt = findViewById(R.id.idEdtMessage)
        sendMsgBtn = findViewById(R.id.idBtnSendMessage)

        // adding on click listener for send message button.
        sendMsgBtn.setOnClickListener {

            // on below line we are creating two
            // variables for phone and message
            val phoneNumber = phoneEdt.text.toString()
            val message = messageEdt.text.toString()

            // on the below line we are creating a try and catch block
            try {


                    val smsManager: SmsManager
                    //if SDK is greater that or equal to 23 then
                    //this is how we will initialize the SmsManager
                    smsManager = this.getSystemService(SmsManager::class.java)

                    // on below line we are sending text message.
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null)

                    // on below line we are displaying a toast message for message send,
                    Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()

                // on below line we are initializing sms manager.
                //as after android 10 the getDefault function no longer works
                //so we have to check that if our android version is greater
                //than or equal toandroid version 6.0 i.e SDK 23

            } catch (e: Exception) {

                // on catch block we are displaying toast message for error.
                Toast.makeText(
                    applicationContext,
                    "Please enter all the data.." + e.message.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }
        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    Log.d("TSF_APPS", "${it.key} = ${it.value}")
                }
            }

    fun doesPhonePeExist(): Boolean {
        var packageInfo: PackageInfo? = null
        var phonePeVersionCode = -1L
        try {
            packageInfo =
                packageManager.getPackageInfo(PHONEPE_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            phonePeVersionCode = packageInfo!!.versionCode.toLong()
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(
                "TSF_APPS", String.format(
                    "failed to get package info for package name = {%s}, exception message = {%s}",
                    PHONEPE_PACKAGE_NAME, e.message
                )
            )
        }
        if (packageInfo == null) {
            return false
        }
        return if (phonePeVersionCode > 94033) {
            true
        } else false
    }
    object Constant{
        const val PHONEPE_PACKAGE_NAME = "com.phonepe.app.preprod"
    }
}