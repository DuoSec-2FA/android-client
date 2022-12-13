package com.twofa.duosec.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.twofa.duosec.databinding.LayoutCompanyCardBinding
import com.twofa.duosec.models.jwt.JwtPayloadDatabase
import com.twofa.duosec.recoverycode.RecoveryCodeActivity
import org.duosec.backendlibrary.HMACAlgorithm
import org.duosec.backendlibrary.TOTP
import java.nio.charset.Charset
import java.time.Duration
import java.util.*


class JwtPayloadAdapter(private val payloadsList: List<JwtPayloadDatabase>):  RecyclerView.Adapter<JwtPayloadAdapter.JwtPayloadViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class JwtPayloadViewHolder(val binding: LayoutCompanyCardBinding) : RecyclerView.ViewHolder(binding.root)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JwtPayloadViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = LayoutCompanyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

                binding.btnShowRecoveryCode.setOnClickListener {
                    val intent: Intent = Intent(holder.itemView.context, RecoveryCodeActivity::class.java)

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

                val totpObj = TOTP
                    .Builder(secret)
                    .withPasswordLength(6)
                    .withAlgorithm(algo)
                    .withPeriod(Duration.ofSeconds(this.otpRefreshDuration * 60L))
                    .build()

                // (System.currentTimeMillis() / 1000) % (this.otpRefreshDuration * 60)
                // amount of time for the current otp to expire in seconds
                (this.otpRefreshDuration * 60) - ((System.currentTimeMillis() / 1000) % (this.otpRefreshDuration * 60))

                val otp: String = totpObj.now()
                binding.tvOtp1.text = otp.get(0).toString()
                binding.tvOtp2.text = otp.get(1).toString()
                binding.tvOtp3.text = otp.get(2).toString()
                binding.tvOtp4.text = otp.get(3).toString()
                binding.tvOtp5.text = otp.get(4).toString()
                binding.tvOtp6.text = otp.get(5).toString()
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = payloadsList.size
}