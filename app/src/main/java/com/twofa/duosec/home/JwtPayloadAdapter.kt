package com.twofa.duosec.home

import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.twofa.duosec.databinding.LayoutCompanyCardBinding
import com.twofa.duosec.models.jwt.JwtPayloadDatabase
import com.twofa.duosec.recoverycode.RecoveryCodeActivity
import org.duosec.backendlibrary.HMACAlgorithm
import org.duosec.backendlibrary.TOTP
import java.nio.charset.Charset
import java.time.Duration
import java.util.*


class JwtPayloadAdapter(private val payloadsList: List<JwtPayloadDatabase>) :
    RecyclerView.Adapter<JwtPayloadAdapter.JwtPayloadViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class JwtPayloadViewHolder(val binding: LayoutCompanyCardBinding) :
        RecyclerView.ViewHolder(binding.root)

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
                binding.tvCompanyLogo.text =
                    this.companyName.get(0).toString().uppercase(Locale.ROOT)
                binding.tvCompanyName.text = this.companyName

                binding.btnShowRecoveryCode.setOnClickListener {
                    val intent: Intent =
                        Intent(holder.itemView.context, RecoveryCodeActivity::class.java)

                    // Putting required data in Intent
                    intent.putExtra("companyName", this.companyName)
                    intent.putExtra("employeeUniqueIdHex", this.employeeUniqueIdHex)

                    holder.itemView.context.startActivity(intent)
                }

                val algo: HMACAlgorithm = if (Objects.equals(
                        this.algorithm,
                        "SHA1"
                    )
                ) HMACAlgorithm.SHA1 else if (Objects.equals(
                        this.algorithm,
                        "SHA256"
                    )
                ) HMACAlgorithm.SHA256 else HMACAlgorithm.SHA512

                val secret: ByteArray = this.secret.toByteArray(Charset.defaultCharset())
                handleUi(algo, secret, otpRefreshDuration, binding)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleUi(
        algo: HMACAlgorithm,
        secret: ByteArray,
        otpRefreshDuration: Int,
        binding: LayoutCompanyCardBinding
    ) {
        val totpObj = TOTP
            .Builder(secret)
            .withPasswordLength(6)
            .withAlgorithm(algo)
            .withPeriod(Duration.ofSeconds(otpRefreshDuration * 60L))
            .build()
        val time =
            (otpRefreshDuration * 60) - ((System.currentTimeMillis() / 1000) % (otpRefreshDuration * 60))
        val otp = totpObj.now()
        object : CountDownTimer(time * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println("seconds remaining: " + millisUntilFinished / 1000)
                binding.tvOtp1.text = otp[0].toString()
                binding.tvOtp2.text = otp[1].toString()
                binding.tvOtp3.text = otp[2].toString()
                binding.tvOtp4.text = otp[3].toString()
                binding.tvOtp5.text = otp[4].toString()
                binding.tvOtp6.text = otp[5].toString()
            }

            override fun onFinish() {
                println("done!")
                handleUi(algo, secret, otpRefreshDuration, binding)
            }
        }.start()
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = payloadsList.size
}