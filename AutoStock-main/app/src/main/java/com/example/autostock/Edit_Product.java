package com.example.autostock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edit_Product extends AppCompatActivity {

    Spinner spinnerID, spinnerCate, spinnerMarca, spinnerProvee;
    EditText txtNombre, txtCantidad;
    Button btn_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        spinnerID = findViewById(R.id.spinnerID);
        spinnerCate = findViewById(R.id.spinnerCate);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        spinnerProvee = findViewById(R.id.spinnerProvee);

        txtNombre = findViewById(R.id.nameET);
        txtCantidad = findViewById(R.id.cantidadET);

        btn_insert = findViewById(R.id.btnInsert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        loadIDSpinner(); // Cargar los IDs en el spinner

        ArrayAdapter<CharSequence> cateAdapter = ArrayAdapter.createFromResource(this, R.array.categorias_array, android.R.layout.simple_spinner_item);
        cateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCate.setAdapter(cateAdapter);

        ArrayAdapter<CharSequence> marcaAdapter = ArrayAdapter.createFromResource(this, R.array.marcas_array, android.R.layout.simple_spinner_item);
        marcaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(marcaAdapter);

        ArrayAdapter<CharSequence> proveeAdapter = ArrayAdapter.createFromResource(this, R.array.proveedores_array, android.R.layout.simple_spinner_item);
        proveeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvee.setAdapter(proveeAdapter);
    }

    private void loadIDSpinner() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando IDs...");

        final ArrayAdapter<String> idAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerID.setAdapter(idAdapter);

        idAdapter.add("ID"); // Agrega "ID" como opción predeterminada

        StringRequest request = new StringRequest(Request.Method.GET, "http://192.168.43.234/android_autostock/recuperarID.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int id = jsonArray.getInt(i);
                                idAdapter.add(String.valueOf(id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Edit_Product.this, "Error al cargar IDs: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Edit_Product.this);
        requestQueue.add(request);
        progressDialog.show();
    }

    private void insertData() {
        final String id = spinnerID.getSelectedItem().toString(); // Utiliza el spinner de ID
        final String nombre = txtNombre.getText().toString().trim();
        final String cantidad = txtCantidad.getText().toString().trim();

        // Solo obtén los valores seleccionados de los spinners si se ha seleccionado algo
        final String categoria = spinnerCate.getSelectedItemPosition() > 0 ? spinnerCate.getSelectedItem().toString() : "";
        final String marca = spinnerMarca.getSelectedItemPosition() > 0 ? spinnerMarca.getSelectedItem().toString() : "";
        final String proveedor = spinnerProvee.getSelectedItemPosition() > 0 ? spinnerProvee.getSelectedItem().toString() : "";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");

        if (id.equals("ID")) {
            Toast.makeText(this, "Seleccione un ID", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.234/android_autostock/editProduct.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.equalsIgnoreCase("datos editados")) {
                                Toast.makeText(Edit_Product.this, "Datos editados", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), fragment_inventory.class));
                                finish();
                            } else {
                                Toast.makeText(Edit_Product.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Edit_Product.this, "Error al realizar la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("nombre", nombre);
                    params.put("cantidad", cantidad);

                    // Solo agrega los parámetros si se ha seleccionado un valor en el spinner correspondiente
                    if (!categoria.isEmpty()) {
                        params.put("categoria", categoria);
                    }
                    if (!marca.isEmpty()) {
                        params.put("marca", marca);
                    }
                    if (!proveedor.isEmpty()) {
                        params.put("proveedor", proveedor);
                    }

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Edit_Product.this);
            requestQueue.add(request);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
