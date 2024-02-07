package com.dbtutorialone

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import kotlinx.coroutines.currentCoroutineContext

class BiometricAuthModule(reactAppContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactAppContext) {

    override fun getName(): String {
        return "BiometricAuthModule"
    }

    @ReactMethod
    fun isBiometricSupported(callback: Callback) {
        val context = currentActivity as FragmentActivity

        val biometricManager = BiometricManager.from(context)
        val result =
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {

                BiometricManager.BIOMETRIC_SUCCESS -> {
                    // showMessage(ErrorCode.BIOMETRIC_SUCCESS.message)
                    true
                }

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    showMessage(ErrorCode.BIOMETRIC_ERROR_NO_HARDWARE.message)
                    false
                }

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    showMessage(ErrorCode.BIOMETRIC_ERROR_HW_UNAVAILABLE.message)
                    false
                }

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    showMessage(ErrorCode.BIOMETRIC_ERROR_NONE_ENROLLED.message)
                    false
                }

                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                    showMessage(ErrorCode.BIOMETRIC_ERROR_UNSUPPORTED.message)
                    false
                }

                else -> {
                    showMessage(ErrorCode.BIOMETRIC_STATUS_UNKNOWN.message)
                    false
                }
            }

        callback.invoke(result)
    }

    @ReactMethod
    fun authenticate(callback: Callback) {
        val activity = currentActivity as FragmentActivity

        Handler(Looper.getMainLooper()).post {
            val biometricAuth = BiometricAuth(activity)
            biometricAuth.authenticate(object : BiometricAuth.BiometricCallback {

                override fun onSuccess() {
                    callback.invoke(null, "Authentication successful")
                }

                override fun onError() {
                    callback.invoke("Authentication failed")
                }
            })

        }
    }

    @ReactMethod
    private fun showMessage(message: String) {
        Toast.makeText(reactApplicationContext, message, Toast.LENGTH_LONG).show()
    }

    private enum class ErrorCode(val value: Int, val message: String) {
        BIOMETRIC_SUCCESS(0, "Biometric Success"),
        BIOMETRIC_STATUS_UNKNOWN(-1, "Biometric status Unknown"),
        BIOMETRIC_ERROR_UNSUPPORTED(-2, "Biometric Error Unsupported"),
        BIOMETRIC_ERROR_HW_UNAVAILABLE(1, "Biometric Error Hardware Unavailable"),
        BIOMETRIC_ERROR_NONE_ENROLLED(11, "Biometric Error None Enrolled"),
        BIOMETRIC_ERROR_NO_HARDWARE(12, "Biometric Error No Hardware"),
        AUTHENTICATION_FAILED(13, "Biometric is registered but not authorised with Current User."),
        AUTHENTICATION_ERROR(14, "Authentication Error");

    }

}

