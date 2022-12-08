package com.twofa.duosec.registration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.twofa.duosec.R
import com.twofa.duosec.recoverycode.RecoveryCodeActivity
import java.util.*


class RegistrationActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private var hintView: TextView? = null
    private var isPermissionGranted: Boolean = false

    private val TAG = "RegistrationActivity"

    companion object {
        private const val RC_PERMISSION = 10
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        hintView = findViewById(R.id.scanner_hint)
        codeScanner = CodeScanner(this, findViewById(R.id.scanner))

//        val moshi = Moshi.Builder().build()
//        val jwtPayloadAdapter = moshi.adapter<JwtPayload>(JwtPayload::class.java)

//        val sharedPreferences: SharedPreferences = getSharedPreferences("DUOSEC", MODE_PRIVATE);
//        val myEdit: SharedPreferences.Editor = sharedPreferences.edit();

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val jwtToken: String = it.text

                val chunks = jwtToken.split('.')
                val (headerChunk, payloadChunk, signatureChunk) = chunks;

                val decoder = Base64.getUrlDecoder();
                val payload = String(decoder.decode(payloadChunk));

                // val jwtPayload: JwtPayload? = jwtPayloadAdapter.fromJson(payload);

                Toast.makeText(this, "Jwt Token: ${jwtToken}", Toast.LENGTH_LONG).show()
                Toast.makeText(this, "Jwt Payload: ${payload}", Toast.LENGTH_LONG).show()
                // Toast.makeText(this, "Jwt Payload: ${jwtPayload!!.toString()}", Toast.LENGTH_LONG).show()

                Log.d(TAG, "Jwt Token " + jwtToken)
                Log.d(TAG, "Jwt Payload"  + payload)
                // Log.d(TAG, "Jwt Token Data Class " + jwtPayload!!.toString())

                val bundle = Bundle()
                bundle.putString("Jwt Token", jwtToken)
                bundle.putString("Jwt Payload", payload)

                val intent = Intent(this@RegistrationActivity, RecoveryCodeActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
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