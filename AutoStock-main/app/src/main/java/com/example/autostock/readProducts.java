package com.example.autostock;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class readProducts extends AppCompatActivity {

    private TextView productsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_products);

        productsTextView = findViewById(R.id.productsTextView);

        // Reemplaza "tu_url_aqui" con la URL completa de tu archivo PHP
        String phpUrl = "http://192.168.43.234/android_autostock/readProduct.php";
        new FetchProductsTask().execute(phpUrl);
    }



    private class FetchProductsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            productsTextView.setText(""); // Limpia el TextView antes de agregar nuevos datos
            parseJSONAndDisplay(result);
        }
    }

    private void parseJSONAndDisplay(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productObj = jsonArray.getJSONObject(i);
                int productId = productObj.getInt("id");
                String productName = productObj.getString("nombre");
                String productCategory = productObj.getString("categoria");
                String productBrand = productObj.getString("marca");
                int productQuantity = productObj.getInt("cantidad");
                String productSupplier = productObj.getString("proveedor");

                productsTextView.append("ID: " + productId + "\n");
                productsTextView.append("Nombre: " + productName + "\n");
                productsTextView.append("Categoría: " + productCategory + "\n");
                productsTextView.append("Marca: " + productBrand + "\n");
                productsTextView.append("Cantidad: " + productQuantity + "\n");
                productsTextView.append("Proveedor: " + productSupplier + "\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}