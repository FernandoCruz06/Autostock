package com.example.autostock;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_converter extends Fragment {

    private Button btnLongitud, btnPeso, btnVolumen, btnTemp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_converter, container, false);
        getActivity().setTitle("Conversor");

        btnLongitud = view.findViewById(R.id.btn_longitud);
        btnLongitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLongitud();
            }
        });

        btnPeso = view.findViewById(R.id.btn_peso);
        btnPeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityPeso();
            }
        });

        btnVolumen = view.findViewById(R.id.btn_volumen);
        btnVolumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityVolumen();
            }
        });

        btnTemp = view.findViewById(R.id.btn_temperatura);
        btnTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityTemperatura();
            }
        });

        return view;
    }

    public void openActivityLongitud() {
        loadFragment(new fragment_longitud());
    }

    public void openActivityPeso() {
        loadFragment(new fragment_peso());
    }

    public void openActivityVolumen() {
        loadFragment(new fragment_volumen());
    }

    public void openActivityTemperatura() {
        loadFragment(new fragment_temperatura());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}