package com.example.autostock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class editar extends AppCompatActivity {

    EditText edNombre, edCategoria, edMarca, edCantidad, edProveedor;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        edNombre = findViewById(R.id.nameED);
        edCategoria = findViewById(R.id.cateED);
        edMarca = findViewById(R.id.marcaED);
        edCantidad = findViewById(R.id.cantidadED);
        edProveedor = findViewById(R.id.proveED);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        edNombre.setText(fragment_inventory.employeeArrayList.get(position).getNombre());
        edCategoria.setText(fragment_inventory.employeeArrayList.get(position).getCategoria());
        edMarca.setText(fragment_inventory.employeeArrayList.get(position).getMarca());
        edCantidad.setText(fragment_inventory.employeeArrayList.get(position).getCantidad());
        edProveedor.setText(fragment_inventory.employeeArrayList.get(position).getProveedor());

    }

    public void actualizar(View view) {
        final String nombre = edNombre.getText().toString();
        final String categoria = edCategoria.getText().toString();
        final String marca = edMarca.getText().toString();
        final String cantidad = edCantidad.getText().toString();
        final String proveedor = edProveedor.getText().toString();


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Actualizando....");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, "https://hola.kimvccug.lucusvirtual.es/actualizar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(editar.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),fragment_inventory.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(editar.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();

                params.put("nombre",nombre);
                params.put("categoria",categoria);
                params.put("marca",marca);
                params.put("cantidad",cantidad);
                params.put("proveedor",proveedor);;

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(editar.this);
        requestQueue.add(request);





    }
}