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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class fragment_volumen extends Fragment implements AdapterView.OnItemSelectedListener{

    String[] items = new String[]{"Litro", "Mililitro", "Metro Cubico", "Galon"};
    private Spinner volumenEntrada;
    private Spinner volumenSalida;
    private EditText et_entrada;
    private TextView et_salida;

    private Button btnRegreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_volumen, container, false);

        volumenEntrada = view.findViewById(R.id.spinner_TempEntrada);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        volumenEntrada.setAdapter(adapter);
        volumenEntrada.setOnItemSelectedListener(this);

        volumenSalida = view.findViewById(R.id.spinner_tempSalida);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, items);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        volumenSalida.setAdapter(adapter2);
        volumenSalida.setOnItemSelectedListener(this);

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
        double Lt = 0;
        double res = 0;

        if (!validarCampos()) return;

        // Detectamos la Unidad de Volumen a convertir para convertirlo a litros y con esa base convertirlos a la Unidad de Volumen de salida seleccionada
        switch (volumenEntrada.getSelectedItem().toString()) {
            case "Litro":
                Lt = Double.parseDouble(et_entrada.getText().toString());
                break;

            case "Mililitro":
                Lt = Double.parseDouble(et_entrada.getText().toString()) / 1000;
                break;

            case "Metro Cubico":
                Lt = Double.parseDouble(et_entrada.getText().toString()) * 1000;
                break;

            case "Galon":
                Lt = Double.parseDouble(et_entrada.getText().toString()) * 3.78541;
                break;
        }

        // Con la conversión anterior a litros, ahora convertimos ese valor en litros a la unidad de volumen seleccionada.
        switch (volumenSalida.getSelectedItem().toString()) {
            case "Litro":
                res = Lt;
                break;

            case "Mililitro":
                res = Lt * 1000;
                break;

            case "Metro Cubico":
                res = Lt / 1000;
                break;

            case "Galon":
                res = Lt / 3.78541;
                break;
        }

        et_salida.setText(Double.toString(res));

    }
}