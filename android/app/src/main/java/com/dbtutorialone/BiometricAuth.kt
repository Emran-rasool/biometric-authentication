package com.dbtutorialone

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity


class BiometricAuth(private val activity: FragmentActivity) {

    fun authenticate(callback: BiometricCallback) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(callback.title)
            .setSubtitle(callback.subTitle)
            .setNegativeButtonText(callback.cancel)
            .build()

        val biometricPrompt = BiometricPrompt(activity, MainThreadExecutor(),
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    callback.onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.onError()
                }

            })

        biometricPrompt.authenticate(promptInfo)
    }

    interface BiometricCallback {
        fun onSuccess()
        fun onError()
        val title: String
        val subTitle: String
        val cancel: String
    }


}
