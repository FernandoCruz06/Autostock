package com.example.autostock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class agregar extends AppCompatActivity {

    private EditText txtNombre, txtCantidad;
    private Spinner spinnerCate, spinnerMarca, spinnerProvee;
    private Button btn_insert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        txtNombre = findViewById(R.id.nameET);
        txtCantidad = findViewById(R.id.cantidadET);

        spinnerCate = findViewById(R.id.spinnerCate);
        spinnerMarca = findViewById(R.id.spinnerMarca);
        spinnerProvee = findViewById(R.id.spinnerProvee);

        btn_insert = findViewById(R.id.btnInsert);

        // Configura el adaptador para los Spinners excluyendo la primera opción
        ArrayAdapter<CharSequence> adapterCate = ArrayAdapter.createFromResource(this,
                R.array.categorias_array, android.R.layout.simple_spinner_item);
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCate.setAdapter(adapterCate);
        spinnerCate.setSelection(0, false); // Muestra "Categoría"

        ArrayAdapter<CharSequence> adapterMarca = ArrayAdapter.createFromResource(this,
                R.array.marcas_array, android.R.layout.simple_spinner_item);
        adapterMarca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter(adapterMarca);
        spinnerMarca.setSelection(0, false); // Muestra "Marca"

        ArrayAdapter<CharSequence> adapterProvee = ArrayAdapter.createFromResource(this,
                R.array.proveedores_array, android.R.layout.simple_spinner_item);
        adapterProvee.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvee.setAdapter(adapterProvee);
        spinnerProvee.setSelection(0, false); // Muestra "Proveedor"


        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSelection()) {
                    insertData();
                } else {
                    Toast.makeText(agregar.this, "Selecciona opciones válidas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidSelection() {
        return !isCaratulaSelected(spinnerCate)
                && !isCaratulaSelected(spinnerMarca)
                && !isCaratulaSelected(spinnerProvee);
    }

    private boolean isCaratulaSelected(Spinner spinner) {
        return spinner.getSelectedItemPosition() == 0;
    }

    private void insertData() {

        final String nombre = txtNombre.getText().toString().trim();
        final String cantidad = txtCantidad.getText().toString().trim();
        final String categoria = spinnerCate.getSelectedItem().toString();
        final String marca = spinnerMarca.getSelectedItem().toString();
        final String proveedor = spinnerProvee.getSelectedItem().toString();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");

        if(nombre.isEmpty()){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(cantidad.isEmpty()){
            Toast.makeText(this, "Ingrese cantidad", Toast.LENGTH_SHORT).show();
            return;
        }


        else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.234/android_autostock/insertProduct.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equalsIgnoreCase("datas insertados")){
                                Toast.makeText(agregar.this, "datas insertados", Toast.LENGTH_SHORT).show();


                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),fragment_inventory.class));
                                finish();
                            }
                            else{
                                Toast.makeText(agregar.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(agregar.this, "Error al realizar la solicitud: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("nombre", txtNombre.getText().toString());
                    params.put("categoria", spinnerCate.getSelectedItem().toString()); // Usamos el valor seleccionado del Spinner
                    params.put("marca", spinnerMarca.getSelectedItem().toString());   // Usamos el valor seleccionado del Spinner
                    params.put("cantidad", txtCantidad.getText().toString());
                    params.put("proveedor", spinnerProvee.getSelectedItem().toString()); // Usamos el valor seleccionado del Spinner

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(agregar.this);
            requestQueue.add(request);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}