package com.example.autostock;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;

import static androidx.biometric.BiometricManager.Authenticators.*;

public class fragment_fingerprint extends Fragment {

    private Button btn_fp, btn_fppin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint, container, false);

        btn_fp = view.findViewById(R.id.btn_fp);
        btn_fppin = view.findViewById(R.id.btn_fppin);

        checkBioMetricSupported();
        Executor executor = ContextCompat.getMainExecutor(requireContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(requireContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), containerTools.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(requireContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        btn_fp.setOnClickListener(view1 -> {
            BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
            promptInfo.setNegativeButtonText("Cancel");
            biometricPrompt.authenticate(promptInfo.build());
        });

        btn_fppin.setOnClickListener(view12 -> {
            BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
            promptInfo.setDeviceCredentialAllowed(true);
            biometricPrompt.authenticate(promptInfo.build());
        });

        return view;
    }

    private BiometricPrompt.PromptInfo.Builder dialogMetric() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in using your biometric credential");
    }

    private void checkBioMetricSupported() {
        BiometricManager manager = BiometricManager.from(requireContext());
        String info = "";
        switch (manager.canAuthenticate(BIOMETRIC_WEAK | BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "App can authenticate using biometrics.";
                enableButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "No biometric features available on this device.";
                enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Biometric features are currently unavailable.";
                enableButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "Need to register at least one fingerprint";
                enableButton(false, true);
                break;
            default:
                info = "Unknown cause";
                enableButton(false);
        }

        TextView txinfo = requireView().findViewById(R.id.tx_info);
        txinfo.setText(info);
    }

    private void enableButton(boolean enable) {
        btn_fp.setEnabled(enable);
        btn_fppin.setEnabled(true);
    }

    private void enableButton(boolean enable, boolean enroll) {
        enableButton(enable);

        if (!enroll) return;
        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
        startActivity(enrollIntent);
    }
}