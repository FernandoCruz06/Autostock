package com.example.autostock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class fragment_temperatura extends Fragment implements AdapterView.OnItemSelectedListener{

    String[] items = new String[]{"Celsius", "Fahrenheit", "Kelvin"};
    private Spinner temperaturaEntrada;
    private Spinner temperaturaSalida;
    private EditText et_entrada;
    private TextView et_salida;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temperatura, container, false);
        getActivity().setTitle("Temperatura");

        temperaturaEntrada = view.findViewById(R.id.spinner_TempEntrada);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temperaturaEntrada.setAdapter(adapter);
        temperaturaEntrada.setOnItemSelectedListener(this);

        temperaturaSalida = view.findViewById(R.id.spinner_tempSalida);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, items);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        temperaturaSalida.setAdapter(adapter2);
        temperaturaSalida.setOnItemSelectedListener(this);

        et_entrada = view.findViewById(R.id.et_entrada);
        et_salida = view.findViewById(R.id.et_salida);
        et_entrada.setText("1");
        et_salida.setFocusable(false);

        et_entrada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Al cambiar el EditText ejecutamos un nuevo cálculo
                Calcular();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Calcular();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    public boolean validarCampos() {
        if (et_entrada.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Ingrese un Valor a Convertir", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_entrada.getText().toString().equals(".")) {
            Toast.makeText(getContext(), "Ingrese un Valor Válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void Calcular() {
        double celsius = 0;
        double res = 0;

        if (!validarCampos()) return;

        // Detectamos la Unidad de Temperatura a convertir para convertirla a Celsius
        switch (temperaturaEntrada.getSelectedItem().toString()) {
            case "Celsius":
                celsius = Double.parseDouble(et_entrada.getText().toString());
                break;

            case "Fahrenheit":
                celsius = (Double.parseDouble(et_entrada.getText().toString()) - 32) * (5.0 / 9.0);
                break;

            case "Kelvin":
                celsius = Double.parseDouble(et_entrada.getText().toString()) - 273.15;
                break;
        }

        // Con la conversión a Celsius, ahora convertimos ese valor a la unidad de temperatura de salida seleccionada
        switch (temperaturaSalida.getSelectedItem().toString()) {
            case "Celsius":
                res = celsius;
                break;

            case "Fahrenheit":
                res = (celsius * (9.0 / 5.0)) + 32;
                break;

            case "Kelvin":
                res = celsius + 273.15;
                break;
        }

        et_salida.setText(Double.toString(res));
    }
}