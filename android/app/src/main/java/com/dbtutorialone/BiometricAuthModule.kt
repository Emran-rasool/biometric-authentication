package com.dbtutorialone

import android.os.Handler
import android.os.Looper
import androidx.biometric.BiometricManager
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class BiometricAuthModule(reactAppContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactAppContext) {

    var titlePrompt: String = ""
    var subTitlePrompt: String = ""
    var cancelPrompt: String = ""

    override fun getName(): String {
        return "BiometricAuthModule"
    }

    @ReactMethod
    fun isBiometricSupported(callback: Callback) {
        val context = currentActivity as FragmentActivity

        val biometricManager = BiometricManager.from(context)
        val result: String =
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {

                BiometricManager.BIOMETRIC_SUCCESS -> ErrorCode.BIOMETRIC_SUCCESS.toString()
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> ErrorCode.BIOMETRIC_ERROR_NO_HARDWARE.toString()
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> ErrorCode.BIOMETRIC_ERROR_HW_UNAVAILABLE.toString()
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> ErrorCode.BIOMETRIC_ERROR_NONE_ENROLLED.toString()
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> ErrorCode.BIOMETRIC_ERROR_UNSUPPORTED.toString()
                else -> ErrorCode.BIOMETRIC_STATUS_UNKNOWN.toString()

            }
        callback.invoke(result)

    }

    @ReactMethod
    fun authenticationPrompt(titleArg: String, subTitleArg: String, cancelArg: String) {
        titlePrompt = titleArg
        subTitlePrompt = subTitleArg
        cancelPrompt = cancelArg

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

                override fun onError(errorCode: Int, errorString: String) {
                    callback.invoke("Authentication Error: $errorString")
                }

                override fun onAuthFailed() {
                    callback.invoke("Authentication failed")
                }

                override val title: String
                    get() = titlePrompt
                override val subTitle: String
                    get() = subTitlePrompt
                override val cancel: String
                    get() = cancelPrompt

            })

        }
    }

    enum class ErrorCode {
        BIOMETRIC_SUCCESS,
        BIOMETRIC_STATUS_UNKNOWN,
        BIOMETRIC_ERROR_UNSUPPORTED,
        BIOMETRIC_ERROR_HW_UNAVAILABLE,
        BIOMETRIC_ERROR_NONE_ENROLLED,
        BIOMETRIC_ERROR_NO_HARDWARE,
        AUTHENTICATION_FAILED,
        AUTHENTICATION_ERROR;

    }

}

