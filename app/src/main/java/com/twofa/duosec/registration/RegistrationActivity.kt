package com.twofa.duosec.registration

import android.Manifest
import android.content.Intent
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
import com.twofa.duosec.R
import com.twofa.duosec.database.DuosecDatabase
import com.twofa.duosec.home.HomeActivity
import com.twofa.duosec.models.jwt.JwtPayloadDatabase
import com.twofa.duosec.models.jwt.JwtPayloadJson
import com.twofa.duosec.utils.MoshiBuilder
import com.twofa.duosec.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        val jwtPayloadAdapter = MoshiBuilder.getJwtPayloadAdapter()

        val dao = DuosecDatabase.getInstance(this).jwtPayloadDao

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val jwtToken: String = it.text

                val dbPayloadObj = MoshiBuilder.getJwtPayloadDatabaseObj(jwtToken)

                CoroutineScope(Dispatchers.IO).launch {
                    dbPayloadObj?.let {
                        dao.insertJwtPayload(it)
                    }

                    withContext(Dispatchers.Main) {
                        startActivity(Intent(this@RegistrationActivity, HomeActivity::class.java))
                    }
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