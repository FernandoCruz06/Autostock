package com.example.autostock;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class fragment_inventory extends Fragment {

    ListView listView;
    Adapter adapter;
    public static ArrayList<Usuarios> employeeArrayList = new ArrayList<>();
    String url = "http://192.168.43.234/android_autostock/readProduct.php";
    Usuarios usuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        listView = view.findViewById(R.id.myListView);
        adapter = new Adapter(getActivity(), employeeArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ProgressDialog progressDialog = new ProgressDialog(getActivity());

                CharSequence[] dialogItem = {"Detalles", "Editar Datos", "Eliminar Datos"};
                builder.setTitle(employeeArrayList.get(position).getNombre());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                startActivity(new Intent(getActivity(), detalles.class)
                                        .putExtra("position", position));
                                break;
                            case 1:
                                startActivity(new Intent(getActivity(), editar.class)
                                        .putExtra("position", position));
                                break;
                            case 2:
                                deleteData(employeeArrayList.get(position).getId());
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        retrieveData();

        Button fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(v);
            }
        });

        /*FloatingActionButton fabEdit = view.findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduct(v);
            }
        });*/

        Button fabEdit = view.findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduct(v);
            }
        });


        Button fabDelete = view.findViewById(R.id.fabDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(v);
            }
        });

        Button fabRead = view.findViewById(R.id.fabRead);
        fabRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProduct(v);
            }
        });

        return view;
    }

    private void deleteData(final String id) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://hola.kimvccug.lucusvirtual.es/eliminar_.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("datos eliminados")) {
                            Toast.makeText(getActivity(), "eliminado correctamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "no se pudo eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void retrieveData() {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        employeeArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String exito = jsonObject.getString("exito");
                            JSONArray jsonArray = jsonObject.getJSONArray("datos");

                            if (exito.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String nombre = object.getString("nombre");
                                    String categoria = object.getString("categoria");
                                    String marca = object.getString("marca");
                                    String cantidad = object.getString("cantidad");
                                    String proveedor = object.getString("proveedor");

                                    usuarios = new Usuarios(id, nombre, categoria, marca, cantidad, proveedor);
                                    employeeArrayList.add(usuarios);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    public void addProduct(View view) {
        startActivity(new Intent(getActivity(), agregar.class));
    }

    public void editProduct(View view) {
        startActivity(new Intent(getActivity(), Edit_Product.class));
    }

    public void deleteProduct(View view) {
        startActivity(new Intent(getActivity(), Delete_Product.class));
    }

    public void viewProduct(View view) {
        startActivity(new Intent(getActivity(), readProducts.class));
    }
}