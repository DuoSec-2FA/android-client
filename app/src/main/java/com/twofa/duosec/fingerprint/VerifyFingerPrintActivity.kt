package com.twofa.duosec.fingerprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twofa.duosec.databinding.ActivityFingerPrintBinding
import com.twofa.duosec.databinding.ActivityVerifyFingerPrintBinding

class VerifyFingerPrintActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyFingerPrintBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyFingerPrintBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}