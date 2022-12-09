package com.twofa.duosec.recoverycode

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.twofa.duosec.databinding.ActivityRecoveryCodeBinding
import java.util.*

class RecoveryCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryCodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "CME GROUP"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnGenerateCodes.setOnClickListener {
            binding.tvCode1.text = generateOTP().toString()
            binding.tvCode2.text = generateOTP().toString()
            binding.tvCode3.text = generateOTP().toString()
            binding.tvCode4.text = generateOTP().toString()
            binding.tvCode5.text = generateOTP().toString()
        }
    }

    private fun generateOTP(): Int {
        val r = Random()
        val low = 1000000000
        val high = 9999999999
        return r.nextInt((high - low).toInt()) + low
    }
}