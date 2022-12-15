package com.twofa.duosec.recoverycode

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twofa.duosec.api.ApiServiceDao
import com.twofa.duosec.api.RetrofitBuilder
import com.twofa.duosec.databinding.ActivityRecoveryCodeBinding
import com.twofa.duosec.models.recovery_code.RecoveryCodeRequest
import com.twofa.duosec.models.recovery_code.RecoveryCodeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RecoveryCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoveryCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryCodeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get data (companyName, companyHexCode or employeeUniqueIdHex) using intent or bundle
        val intent: Intent = intent
        // get above data from individual recycler view card either by long pressing card or by button
        val companyName: String? = intent.getStringExtra("companyName")
        val employeeUniqueIdHex: String? = intent.getStringExtra("employeeUniqueIdHex")

        companyName?.let {
            supportActionBar?.title = it
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        employeeUniqueIdHex?.let {
            setRecoveryCode(RecoveryCodeRequest(it, false))
        }

        binding.btnGenerateRecoveryCode.setOnClickListener {
            employeeUniqueIdHex?.let {
                setRecoveryCode(RecoveryCodeRequest(it, true))
            }
        }
    }

    fun setRecoveryCode(recoveryCodeRequest: RecoveryCodeRequest) {
        val dao: ApiServiceDao = RetrofitBuilder.apiService

        CoroutineScope(Dispatchers.IO).launch {
            val response: Response<RecoveryCodeResponse> = dao.getRecoveryCode(recoveryCodeRequest)
            if(response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val data: RecoveryCodeResponse? = response.body()
                    data?.let {
                        binding.tvRecoveryCode.text = it.recoveryCode
                    }
                }
            }
        }
    }
}