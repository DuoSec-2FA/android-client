package com.twofa.duosec.fingerprint

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twofa.duosec.databinding.ActivityVerifyFingerPrintBinding
import com.twofa.duosec.home.HomeActivity

class VerifyFingerPrintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyFingerPrintBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyFingerPrintBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "Verify FingerPrint"

        binding.btnVerifyFingerPrint.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}