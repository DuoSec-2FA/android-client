package com.twofa.duosec.utils

object TotpBuilder {
//    val secret: ByteArray = this.secret.toByteArray(Charset.defaultCharset())
//
//    val algo: HMACAlgorithm = if (Objects.equals(
//            this.algorithm,
//            "SHA1"
//        )
//    ) HMACAlgorithm.SHA1 else if (Objects.equals(
//            this.algorithm,
//            "SHA256"
//        )
//    ) HMACAlgorithm.SHA256 else HMACAlgorithm.SHA512
//
//    private val totpBuilder: TOTP = TOTP
//        .Builder(secret)
//        .withPasswordLength(6)
//        .withAlgorithm(algo)
//        .withPeriod(Duration.ofSeconds(this.otpRefreshDuration * 60L))
//        .build()
//
//    fun getTotpBuilder(secret: ByteArray, algorithm: String, otpRefreshDuration: String): TOTP {
//        return totpBuilder
//    }
}