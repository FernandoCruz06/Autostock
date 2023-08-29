package com.example.autostock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class detalles extends AppCompatActivity {

    TextView tvid,tvname,tvcategoria,tvmarca,tvcantidad,tvproveedor;
    int position;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);


        //Initializing Views
        tvid = findViewById(R.id.txtid);
        tvname = findViewById(R.id.txtname);
        tvcategoria = findViewById(R.id.txtcategoria);
        tvmarca = findViewById(R.id.txtmarca);
        tvcantidad = findViewById(R.id.txtcantidad);
        tvproveedor = findViewById(R.id.txtproveedor);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        tvid.setText("ID: " + fragment_inventory.employeeArrayList.get(position).getNombre());
        tvname.setText("Nombre: " + fragment_inventory.employeeArrayList.get(position).getNombre());
        tvcategoria.setText("Categoria: " + fragment_inventory.employeeArrayList.get(position).getCategoria());
        tvmarca.setText("Marca: " + fragment_inventory.employeeArrayList.get(position).getMarca());
        tvcantidad.setText("Cantidad: " + fragment_inventory.employeeArrayList.get(position).getCantidad());
        tvproveedor.setText("Proveedor: " + fragment_inventory.employeeArrayList.get(position).getProveedor());

    }
}