package com.example.autostock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button signInButton;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtname;
    private EditText int_id;

    private SharedPreferences sharedPreferences;
    public String sUser, sEmail;

    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferencesout;
    private static final String SHARED_PREF_OUT = "out";
    private static final String KEY_OUT = "isLoggedIn";


    private static final String SHARED_PREF = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ID = "id";

    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.userET);
        txtPass = findViewById(R.id.passET);
        txtname = findViewById(R.id.nameETsave);
        int_id = findViewById(R.id.idETsave);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        sharedPreferences1 = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        sharedPreferencesout = getSharedPreferences(SHARED_PREF_OUT, MODE_PRIVATE);

        rq = Volley.newRequestQueue(this);

        signInButton = findViewById(R.id.inicioBtn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBtnIniciarSesion(view);
            }
        });

        TextView signUpText = findViewById(R.id.signUpText);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegister(view);
            }
        });

        // Check if the user is already logged in
        if (isLoggedIn()) {
            redirectToHomeApp();
        }
    }

    public void clickBtnIniciarSesion(View view) {
        final View finalView = view; // Declare a final copy of the view variable

        String email = txtEmail.getText().toString().trim();
        String password = txtPass.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, "Por favor completa todos los campos", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Perform user authentication
        authenticateUser(email, password, finalView); // Pass the finalView variable

        String url = "http://192.168.43.234/android_autostock/get_User.php?email="+"%27"+txtEmail.getText().toString()+"%27";

        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response.length()==1){
                            try {
                                JSONObject object = new JSONObject(response.get(0).toString());
                                txtname.setText(object.getString("name"));
                                int_id.setText(object.getString("id"));

                                SharedPreferences.Editor User_data = sharedPreferences1.edit();
                                User_data.putString(KEY_EMAIL, txtEmail.getText().toString());
                                User_data.putString(KEY_PASSWORD, txtPass.getText().toString());
                                User_data.putString(KEY_NAME, txtname.getText().toString());
                                User_data.putString(KEY_ID, int_id.getText().toString());
                                User_data.commit();
                                User_data.apply();

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            // Handle errors
                            Snackbar.make(view, "An error occurred", Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Snackbar.make(view, "An error occurred", Snackbar.LENGTH_LONG).show();
                    }
                });
        rq.add(requerimiento);

    }

    private void authenticateUser(final String email, final String password, final View view) {
        String url = "http://192.168.43.234/android_autostock/login.php"; // Update the URL with the correct path to your PHP file

        // Create a request queue using Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a string request to send the user credentials to the PHP script
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                                // Handle the response from the PHP script
                                if (response.equals("Login successful")) {
                                    // User login successful, perform desired actions
                                    Snackbar.make(view, "Login successful", Snackbar.LENGTH_LONG).show();
                                    saveLoginStatus(); // Save the login status

                                    redirectToHomeApp(); // Redirect to the HomeApp activity



                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("user", sUser);
                                    editor.putString("email", sEmail);
                                    editor.apply();

                            } else {
                                // Invalid credentials
                                Snackbar.make(view, "Invalid credentials", Snackbar.LENGTH_LONG).show();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Snackbar.make(view, "An error occurred", Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Pass the user credentials to the PHP script
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        // Add the request to the queue
        queue.add(stringRequest);
    }

    private void redirectToHomeApp() {
            Intent intent2 = new Intent(LoginActivity.this, container.class);
            startActivity(intent2);
            finish();
        }

    private void saveUserData(View v) {
        String url = "http://192.168.43.234/android_autostock/get_User.php?email="+"%27"+txtEmail.getText().toString()+"%27";

        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response.length()==1){
                            try {
                                JSONObject object = new JSONObject(response.get(0).toString());
                                txtname.setText(object.getString("name"));
                                int_id.setText(object.getString("id"));


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            // Handle errors
                            Snackbar.make(v, "An error occurred", Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Snackbar.make(v, "An error occurred", Snackbar.LENGTH_LONG).show();
                    }
                });
        rq.add(requerimiento);
    }

    private void onClickRegister(View view) {
        Intent intent1 = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent1);
    }

    public void onClickHuella(View view) {
        sharedPreferences1 = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String name = sharedPreferences1.getString(KEY_NAME,null);
        String email = sharedPreferences1.getString(KEY_EMAIL,null);

        if(name != null && email != null) {
            Intent intent = new Intent(LoginActivity.this, LoginHuella.class);
            startActivity(intent);
        }else {
            Snackbar.make(view, "Invalid credentials", Snackbar.LENGTH_LONG).show();
        }
    }


    private void saveLoginStatus() {
        SharedPreferences.Editor out = sharedPreferencesout.edit();
        out.putBoolean(KEY_OUT, true);
        out.apply();
    }

    public void forgotPasswordClicked(View view) {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
    }

    private boolean isLoggedIn() {
        return sharedPreferencesout.getBoolean(KEY_OUT, false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
