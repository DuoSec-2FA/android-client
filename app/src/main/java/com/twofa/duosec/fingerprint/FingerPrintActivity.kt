package com.twofa.duosec.fingerprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twofa.duosec.databinding.ActivityFingerPrintBinding

class FingerPrintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFingerPrintBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFingerPrintBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}