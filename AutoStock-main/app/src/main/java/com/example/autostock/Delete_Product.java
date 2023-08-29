package com.example.autostock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Delete_Product extends AppCompatActivity {

        Spinner idSpinner;
        Button btn_insert;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_delete_product);

                idSpinner = findViewById(R.id.IDSpinner);
                btn_insert = findViewById(R.id.btnInsert);

                ArrayAdapter<String> idAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
                idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                idSpinner.setAdapter(idAdapter);

                idAdapter.add("ID"); // Agrega "ID" como opción predeterminada
                obtenerListaDeIds(idAdapter);

                btn_insert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                showConfirmationDialog();
                        }
                });
        }

        private void showConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmar Eliminación");
                builder.setMessage("¿Estás seguro de que deseas eliminar este producto?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                insertData();
                        }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                        }
                });
                builder.show();
        }

        private void obtenerListaDeIds(final ArrayAdapter<String> adapter) {
                StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.43.234/android_autostock/recuperarID.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                                List<String> idList = new ArrayList<>();
                                try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                int id = jsonArray.getInt(i);
                                                idList.add(String.valueOf(id));
                                        }
                                        adapter.addAll(idList);
                                        adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                        }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                        }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
        }

        private void insertData() {
                final String selectedId = idSpinner.getSelectedItem().toString();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Cargando...");

                if (selectedId.equals("ID")) {
                        Toast.makeText(this, "Seleccione un ID", Toast.LENGTH_SHORT).show();
                        return;
                } else {
                        progressDialog.show();
                        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.234/android_autostock/deleteProduct.php", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                if (jsonResponse.has("success")) {
                                                        boolean success = jsonResponse.getBoolean("success");
                                                        if (success) {
                                                                Toast.makeText(Delete_Product.this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getApplicationContext(), fragment_inventory.class));
                                                                finish();
                                                        } else {
                                                                Toast.makeText(Delete_Product.this, "No se encontró el producto o no se realizaron cambios", Toast.LENGTH_SHORT).show();
                                                        }
                                                } else if (jsonResponse.has("error")) {
                                                        String error = jsonResponse.getString("error");
                                                        Toast.makeText(Delete_Product.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                                }
                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(Delete_Product.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                                        }
                                }
                        }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(Delete_Product.this, "Error al realizar la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                }
                        }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("id", selectedId);
                                        return params;
                                }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(Delete_Product.this);
                        requestQueue.add(request);
                }
        }

        @Override
        public void onBackPressed() {
                super.onBackPressed();
                finish();
        }
}
