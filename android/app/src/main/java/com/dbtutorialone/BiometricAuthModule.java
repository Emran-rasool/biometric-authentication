package com.dbtutorialone;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class BiometricAuthModule extends ReactContextBaseJavaModule {
    private String titlePrompt = "";
    private String subTitlePrompt = "";
    private String cancelPrompt = "";

    public BiometricAuthModule(ReactApplicationContext reactAppContext) {
        super(reactAppContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "BiometricAuthModule";
    }

    @ReactMethod
    public void isBiometricSupported(Callback callback) {
        FragmentActivity activity = (FragmentActivity) getCurrentActivity();
        if (activity == null) {
            callback.invoke("Activity is null");
            return;
        }

        BiometricManager biometricManager = BiometricManager.from(activity);
        int biometricAuthResult = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
        String result;
        switch (biometricAuthResult) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                result = ErrorCode.BIOMETRIC_SUCCESS;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                result = ErrorCode.BIOMETRIC_ERROR_NO_HARDWARE;
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                result = ErrorCode.BIOMETRIC_ERROR_HW_UNAVAILABLE;
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                result = ErrorCode.BIOMETRIC_ERROR_NONE_ENROLLED;
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                result = ErrorCode.BIOMETRIC_ERROR_UNSUPPORTED;
                break;
            default:
                result = ErrorCode.BIOMETRIC_STATUS_UNKNOWN;
        }
        callback.invoke(result);
    }

    @ReactMethod
    public void authenticationPrompt(String titleArg, String subTitleArg, String cancelArg) {
        titlePrompt = titleArg;
        subTitlePrompt = subTitleArg;
        cancelPrompt = cancelArg;
    }

    @ReactMethod
    public void authenticate(final Callback callback) {
        final FragmentActivity activity = (FragmentActivity) getCurrentActivity();
        if (activity == null) {
            callback.invoke("Activity is null");
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                BiometricAuth biometricAuth = new BiometricAuth(activity);
                biometricAuth.authenticate(new BiometricAuth.BiometricCallback() {
                    @Override
                    public void onSuccess() {
                        callback.invoke(null, "Authentication successful");
                    }

                    @Override
                    public void onError(int errorCode, @NonNull String errorString) {
                        callback.invoke("Authentication Error: " + errorString);
                    }

                    @Override
                    public void onAuthFailed() {
                        callback.invoke("Authentication failed");
                    }

                    @NonNull
                    @Override
                    public String getTitle() {
                        return titlePrompt;
                    }

                    @NonNull
                    @Override
                    public String getSubTitle() {
                        return subTitlePrompt;
                    }

                    @NonNull
                    @Override
                    public String getCancel() {
                        return cancelPrompt;
                    }
                });
            }
        });
    }

    public interface ErrorCode {
        String BIOMETRIC_SUCCESS = "BIOMETRIC_SUCCESS";
        String BIOMETRIC_STATUS_UNKNOWN = "BIOMETRIC_STATUS_UNKNOWN";
        String BIOMETRIC_ERROR_UNSUPPORTED = "BIOMETRIC_ERROR_UNSUPPORTED";
        String BIOMETRIC_ERROR_HW_UNAVAILABLE = "BIOMETRIC_ERROR_HW_UNAVAILABLE";
        String BIOMETRIC_ERROR_NONE_ENROLLED = "BIOMETRIC_ERROR_NONE_ENROLLED";
        String BIOMETRIC_ERROR_NO_HARDWARE = "BIOMETRIC_ERROR_NO_HARDWARE";
        String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
        String AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR";
    }
}
