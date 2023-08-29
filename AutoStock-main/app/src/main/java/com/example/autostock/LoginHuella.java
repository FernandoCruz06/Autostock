package com.example.autostock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class LoginHuella extends AppCompatActivity {

    private Button btn_fph, btn_fppinh;
    private SharedPreferences sharedPreferences;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_huella);

        btn_fph = findViewById(R.id.btn_fph);
        btn_fppinh = findViewById(R.id.btn_fppinh);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        /*if (isLoggedIn) {
            redirect ToHomeApp();
        }*/

        checkBioMetricSupported();
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LoginHuella.this,
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LoginHuella.this,
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                // Guardar el estado de inicio de sesión en SharedPreferences
                saveLoginStatus();

                // Deshabilitar el botón de retroceso
                isLoggedIn = true;
                enableButton(false);

                // Redirigir al usuario a la actividad MainActivity
                Intent intent = new Intent(LoginHuella.this, container.class);
                startActivity(intent);
                finish(); // Opcional: Finalizar la actividad actual si deseas que el usuario no pueda regresar presionando el botón de retroceso
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginHuella.this, "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        btn_fph.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
            promptInfo.setNegativeButtonText("Cancel");
            biometricPrompt.authenticate(promptInfo.build());
        });

        btn_fppinh.setOnClickListener(view -> {
            BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
            promptInfo.setDeviceCredentialAllowed(true);
            biometricPrompt.authenticate(promptInfo.build());
        });
    }

    private BiometricPrompt.PromptInfo.Builder dialogMetric() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in using your biometric credential");
    }

    private void checkBioMetricSupported() {
        BiometricManager manager = BiometricManager.from(this);
        String info = "";
        switch (manager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
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

        TextView txinfo = findViewById(R.id.tx_info);
        txinfo.setText(info);
    }

    private void enableButton(boolean enable) {
        btn_fph.setEnabled(enable);
        btn_fppinh.setEnabled(true);
    }

    private void enableButton(boolean enable, boolean enroll) {
        enableButton(enable);

        if (!enroll) return;
        final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
        startActivity(enrollIntent);
    }

    private void saveLoginStatus() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isLoggedIn) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
