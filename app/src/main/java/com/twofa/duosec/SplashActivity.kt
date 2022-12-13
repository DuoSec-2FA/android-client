package com.twofa.duosec

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.twofa.duosec.databinding.ActivitySplashBinding
import com.twofa.duosec.fingerprint.VerifyFingerPrintActivity
import com.twofa.duosec.home.HomeActivity
import com.twofa.duosec.registration.RegistrationActivity
import com.twofa.duosec.utils.hideActionBar
import org.duosec.backendlibrary.SecretGenerator

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIMEOUT: Long = 1500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        println(SecretGenerator.generate().toString())

        setFullscreen()

        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, SPLASH_TIMEOUT)
    }

    private fun setFullscreen() {
        hideActionBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

}
