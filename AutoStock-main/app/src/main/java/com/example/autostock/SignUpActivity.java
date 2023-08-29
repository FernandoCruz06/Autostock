package com.example.autostock;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtConfirmPass;

    private boolean passwordShowing = false;
    private boolean conPasswordShowin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AppCompatButton signUpButton = findViewById(R.id.inicioSesionBtn);
        txtNombre = findViewById(R.id.nameET);
        txtEmail = findViewById(R.id.emailET);
        txtPass = findViewById(R.id.passwordET);
        txtConfirmPass = findViewById(R.id.conPasswordET);
    }

    public void clickReturnLogin(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void clickBtnInsertar(View view) {
        String nombre = txtNombre.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String password = txtPass.getText().toString().trim();
        String confirmarPassword = txtConfirmPass.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, "Por favor, completa todos los campos", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmarPassword)) {
            Snackbar.make(view, "Lo sentimos, las contraseñas no coinciden", Snackbar.LENGTH_LONG).show();
            return;
        }

        String url = "http://192.168.43.234/android_autostock/insertar.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Snackbar.make(view, "Felicidades usuario agregado exitosamente", Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view, "Error " + error, Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", txtNombre.getText().toString());
                params.put("email", txtEmail.getText().toString());
                params.put("password", txtPass.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    /*public void OnClickLogin(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }*/

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            AppCompatButton signUpButton = findViewById(R.id.inicioSesionBtn);

            // Establece la posición inicial del botón fuera de la pantalla
            signUpButton.setTranslationX(signUpButton.getWidth());

            // Crea y configura la animación
            signUpButton.animate()
                    .translationX(0f)
                    .setDuration(1000)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            // Realiza la acción deseada después de la animación, por ejemplo, iniciar otra actividad
                        }
                    })
                    .start(); // Inicia la animación
        }
    }
}
