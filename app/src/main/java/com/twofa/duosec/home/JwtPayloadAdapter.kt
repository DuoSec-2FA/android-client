package com.twofa.duosec.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.twofa.duosec.api.RetrofitBuilder
import com.twofa.duosec.api.network.ConnectivityObserver
import com.twofa.duosec.api.network.ConnectivityStatus
import com.twofa.duosec.database.DuosecDatabase
import com.twofa.duosec.databinding.LayoutCompanyCardBinding
import com.twofa.duosec.models.jwt.JwtPayloadDatabase
import com.twofa.duosec.models.jwt.JwtPayloadJson
import com.twofa.duosec.models.jwt.RegenerateJwtResponse
import com.twofa.duosec.recoverycode.RecoveryCodeActivity
import com.twofa.duosec.utils.MoshiBuilder
import com.twofa.duosec.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.duosec.backendlibrary.HMACAlgorithm
import org.duosec.backendlibrary.TOTP
import java.nio.charset.Charset
import java.time.Duration
import java.util.*

class JwtPayloadAdapter(private val context: Context, private val connectivityObserver: ConnectivityObserver, private val payloadsList: List<JwtPayloadDatabase>):  RecyclerView.Adapter<JwtPayloadAdapter.JwtPayloadViewHolder>() {

    inner class JwtPayloadViewHolder(val binding: LayoutCompanyCardBinding) : RecyclerView.ViewHolder(binding.root)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JwtPayloadViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding: LayoutCompanyCardBinding =
            LayoutCompanyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JwtPayloadViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: JwtPayloadViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val payload = payloadsList[position]

        with(holder) {
            with(payload) {
                binding.tvCompanyLogo.text = this.companyName.get(0).toString().uppercase(Locale.ROOT)
                binding.tvCompanyName.text = this.companyName

                val secret: ByteArray = this.secret.toByteArray(Charset.defaultCharset())

                val algo: HMACAlgorithm = if (Objects.equals(
                        this.algorithm,
                        "SHA1"
                    )
                ) HMACAlgorithm.SHA1 else if (Objects.equals(
                        this.algorithm,
                        "SHA256"
                    )
                ) HMACAlgorithm.SHA256 else HMACAlgorithm.SHA512

                handleUi(algo, secret, this.otpRefreshDuration, this.exp, this.employeeUniqueIdHex, binding)

//                binding.btnShowOtp.setOnClickListener{
//                    if(!Utils.isJwtExpired(this.exp)) {
//                        binding.btnShowOtp.isEnabled = false
//                        handleUi(algo, secret, this.otpRefreshDuration, this.exp, binding)
//                    } else {
//                        connectivityObserver.observe().onEach {
//                            if(it == ConnectivityStatus.AVAILABLE) {
//                                regenerateJwt(this.employeeUniqueIdHex)
//                            } else {
//                                Toast.makeText(context, "Please connect to internet to view otp", Toast.LENGTH_LONG).show()
//                            }
//                        }.launchIn(CoroutineScope(Dispatchers.Main))
//                    }
//                }

                binding.btnShowCode.setOnClickListener {
                    val intent: Intent =
                        Intent(
                            holder.itemView.context,
                            RecoveryCodeActivity::class.java
                        )
                    intent.putExtra("companyName", this.companyName)
                    intent.putExtra("employeeUniqueIdHex", this.employeeUniqueIdHex)
                    context.startActivity(intent)

//                    connectivityObserver.observe().onEach {
//                        println("Internet Status ${it.name}")
//
//                        if (it == ConnectivityStatus.AVAILABLE) {
//                            val intent: Intent =
//                                Intent(
//                                    holder.itemView.context,
//                                    RecoveryCodeActivity::class.java
//                                )
//                            intent.putExtra("companyName", this.companyName)
//                            intent.putExtra("employeeUniqueIdHex", this.employeeUniqueIdHex)
//                            context.startActivity(intent)
//
//                        } else {
//                            Toast.makeText(
//                                context,
//                                "Please connect to internet to view recovery code",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }.launchIn(CoroutineScope(Dispatchers.Main))
                }
            }
        }
    }

    override fun getItemCount() = payloadsList.size

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleUi(
        algo: HMACAlgorithm,
        secret: ByteArray,
        otpRefreshDuration: Int,
        expiryTime: Long,
        employeeUniqueIdHex: String,
        binding: LayoutCompanyCardBinding
    ) {
        val totpObj = TOTP
            .Builder(secret)
            .withPasswordLength(6)
            .withAlgorithm(algo)
            .withPeriod(Duration.ofSeconds(otpRefreshDuration * 60L))
            .build()

        val otp = totpObj.now()

        val time = (otpRefreshDuration * 60) - ((System.currentTimeMillis() / 1000) % (otpRefreshDuration * 60))

        object : CountDownTimer(time * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val totalSecsRemaining = millisUntilFinished / 1000
                val mins: Long = totalSecsRemaining / 60
                val secs = totalSecsRemaining - (mins * 60)
                binding.tvTimeToExpire.text = "$mins mins\n$secs secs"

                setOtp(otp, binding)
            }

            override fun onFinish() {
                if(!Utils.isJwtExpired(expiryTime)) {
                    handleUi(algo, secret, otpRefreshDuration, expiryTime, employeeUniqueIdHex, binding)
                } else {
                    regenerateJwt(employeeUniqueIdHex)
                    handleUi(algo, secret, otpRefreshDuration, expiryTime, employeeUniqueIdHex, binding)

//                    connectivityObserver.observe().onEach {
//                        if(it == ConnectivityStatus.AVAILABLE) {
//                            regenerateJwt(employeeUniqueIdHex)
//                        } else {
//                            Toast.makeText(context, "Please connect to internet to view otp", Toast.LENGTH_LONG).show()
//                        }
//                    }.launchIn(CoroutineScope(Dispatchers.Main))
                }
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun regenerateJwt(companyHex: String) {
        val apiServiceDao = RetrofitBuilder.apiService
        val dbDao = DuosecDatabase.getInstance(context).jwtPayloadDao

        CoroutineScope(Dispatchers.IO).launch {
            val res = apiServiceDao.regenerateJwtToken(companyHex)
            val jwtResponse: RegenerateJwtResponse? = res.body()
            
            if(res.isSuccessful && res.code() == 200 && jwtResponse != null) {
                val dbPayloadObj = MoshiBuilder.getJwtPayloadDatabaseObj(jwtResponse.jwtToken)

                dbPayloadObj?.let {
                    dbDao.updateJwtPayload(it)
                }
            }
        }
    }

    private fun setOtp(otp: String, binding: LayoutCompanyCardBinding) {
        binding.tvOtp1.text = otp[0].toString()
        binding.tvOtp2.text = otp[1].toString()
        binding.tvOtp3.text = otp[2].toString()
        binding.tvOtp4.text = otp[3].toString()
        binding.tvOtp5.text = otp[4].toString()
        binding.tvOtp6.text = otp[5].toString()
    }
}
