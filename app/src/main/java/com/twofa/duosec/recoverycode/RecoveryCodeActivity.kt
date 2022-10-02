package com.twofa.duosec.recoverycode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twofa.duosec.databinding.ActivityHomeBinding
import com.twofa.duosec.databinding.ActivityRecoveryCodeBinding

class RecoveryCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryCodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}