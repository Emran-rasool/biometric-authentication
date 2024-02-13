package com.dbtutorialone;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

public class BiometricAuth {
    private FragmentActivity activity;

    public BiometricAuth(FragmentActivity activity) {
        this.activity = activity;
    }

    public void authenticate(BiometricCallback callback) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(callback.getTitle())
                .setSubtitle(callback.getSubTitle())
                .setNegativeButtonText(callback.getCancel())
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, new MainThreadExecutor(),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        callback.onError(errorCode, errString.toString());
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        callback.onSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        callback.onAuthFailed();
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }

    public interface BiometricCallback {
        void onSuccess();

        void onError(int errorCode, String errorString);

        void onAuthFailed();

        String getTitle();

        String getSubTitle();

        String getCancel();
    }
}
