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

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class fragment_peso extends Fragment implements AdapterView.OnItemSelectedListener{

    String[] items = new String[]{"Tonelada", "Kilogramo", "Gramo", "Onza", "Libra"};
    private Spinner pesosEntrada;
    private Spinner pesosSalida;
    private EditText et_entrada;
    private TextView et_salida;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peso, container, false);

        pesosEntrada = view.findViewById(R.id.spinner_TempEntrada);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pesosEntrada.setAdapter(adapter);
        pesosEntrada.setOnItemSelectedListener(this);

        pesosSalida = view.findViewById(R.id.spinner_tempSalida);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, items);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pesosSalida.setAdapter(adapter2);
        pesosSalida.setOnItemSelectedListener(this);

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
                //Al cambiar el EditText ejecutamos un nuevo cálculo
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

    public boolean validarCampos(){
        if(et_entrada.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Ingrese un Valor a Convertir", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(et_entrada.getText().toString().equals(".")){
            Toast.makeText(getContext(), "Ingrese un Valor Válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void Calcular(){

        double Kg = 0;
        double res = 0;

        if(!validarCampos()) return;

        //Detectamos la Unidad de Peso a convertir para convertirlo a kilogramos y con esa base convertirlos a la Unidad de Peso de salida seleccionada
        switch (pesosEntrada.getSelectedItem().toString()){
            case "Tonelada":
                Kg = Double.parseDouble(et_entrada.getText().toString()) * 1000;
                break;

            case "Kilogramo":
                Kg = Double.parseDouble(et_entrada.getText().toString());
                break;

            case "Gramo":
                Kg = Double.parseDouble(et_entrada.getText().toString()) / 1000;
                break;

            case "Onza":
                Kg = Double.parseDouble(et_entrada.getText().toString()) * 0.0283495;
                break;

            case "Libra":
                Kg = Double.parseDouble(et_entrada.getText().toString()) / 2.20462;
                break;
        }

        //Con la conversión anterior a Kg ahora convertimos ese valor en Kilogramo a la unidad de peso seleccionada.
        switch (pesosSalida.getSelectedItem().toString()){
            case "Tonelada":
                res = Kg / 1000;
                break;

            case "Kilogramo":
                res = Kg;
                break;

            case "Gramo":
                res = Kg * 1000;
                break;

            case "Onza":
                res = Kg / 0.0283495;
                break;

            case "Libra":
                res = Kg * 2.20462;
                break;
        }

        et_salida.setText(Double.toString(res));
    }
}