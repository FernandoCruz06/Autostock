package com.example.autostock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class activity_edit extends AppCompatActivity {

    private static final String PASSWORD_RESET_URL1 = "http://192.168.43.234android_autostock/userUpdate.php";

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText passwordNewEditText;
    private TextInputEditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        nameEditText = findViewById(R.id.recuUserET);
        emailEditText = findViewById(R.id.recuEmailET);
        passwordEditText = findViewById(R.id.recuPassET);
        passwordNewEditText = findViewById(R.id.recuPassnewET);

    }


    public void onClickRecuperar(View view) {
        final String email = emailEditText.getText().toString().trim();
        final String name = nameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String newPassword = passwordNewEditText.getText().toString().trim();

        if (email.isEmpty() || newPassword.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Realiza la solicitud HTTP para actualizar la contraseña
            StringRequest stringRequest = new StringRequest(Request.Method.POST, PASSWORD_RESET_URL1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(activity_edit.this, "Se han actualizado los datos exitosamente", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity_edit.this, container_profile.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity_edit.this, "Contraseña no valida", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("Password", password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            AppCompatButton recuperarBtn = findViewById(R.id.recuperarBtn);

            // Establece la posición inicial del botón fuera de la pantalla
            recuperarBtn.setTranslationX(recuperarBtn.getWidth());

            // Crea y configura la animación
            recuperarBtn.animate()
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