package com.twofa.duosec.registration

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.squareup.moshi.Moshi
import com.twofa.duosec.R
import com.twofa.duosec.models.JwtPayload
import java.util.*


class RegistrationActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private var hintView: TextView? = null
    private var isPermissionGranted: Boolean = false

    companion object {
        private const val RC_PERMISSION = 10
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        hintView = findViewById(R.id.scanner_hint)
        codeScanner = CodeScanner(this, findViewById(R.id.scanner))

//        Jwt Decouple Code
//        val moshi = Moshi.Builder().build()
//        val jwtPayloadAdapter = moshi.adapter(JwtPayload::class.java)
//        val jwtPayload: JwtPayload? = jwtPayloadAdapter.fromJson(payload)

        val sharedPref = getSharedPreferences(getString(R.string.shared_pref_jwt), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val jwtToken: String = it.text

                val chunks = jwtToken.split('.')
                val (header, payloadChunk, _) = chunks

                val decoder = Base64.getUrlDecoder()
                val payload = String(decoder.decode(payloadChunk))

                editor.apply {
                    putString("jwtPayload", payload)
                }
            }
        }

        codeScanner.errorCallback = ErrorCallback { error: Throwable ->
            runOnUiThread {
                Toast.makeText(this, getString(R.string.scanner_error, error), Toast.LENGTH_LONG)
                    .show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = false
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    RC_PERMISSION
                )
            } else {
                isPermissionGranted = true
            }
        } else {
            isPermissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == RC_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
                codeScanner.startPreview()
            } else {
                isPermissionGranted = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}