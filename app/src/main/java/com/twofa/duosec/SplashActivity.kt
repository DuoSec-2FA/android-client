package com.twofa.duosec

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.twofa.duosec.databinding.ActivitySplashBinding
import com.twofa.duosec.fingerprint.VerifyFingerPrintActivity
import com.twofa.duosec.registration.RegistrationActivity
import com.twofa.duosec.utils.hideActionBar

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setFullscreen()

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, VerifyFingerPrintActivity::class.java))
        }

        binding.btnReg.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

//        Handler().postDelayed({
//            binding.btnLogin.setOnClickListener {  }
//            startActivity(Intent(this, WelcomeActivity::class.java))
//            finish()
//        }, SPLASH_TIMEOUT)
    }

    private fun setFullscreen() {
        hideActionBar()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}