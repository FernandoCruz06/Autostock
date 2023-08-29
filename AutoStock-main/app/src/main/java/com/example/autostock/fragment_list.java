package com.example.autostock;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class fragment_list extends Fragment {

    private ListView todo;
    private EditText taskValue;
    private ArrayList<String> listaValores;
    private ArrayAdapter<String> adapterValores;
    private AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        todo = view.findViewById(R.id.taskList);
        todo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                verificar(i);
            }
        });

        taskValue = view.findViewById(R.id.taskValue);
        listaValores = new ArrayList<>();
        builder = new AlertDialog.Builder(requireContext());
        adapterValores = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, listaValores);
        todo.setAdapter(adapterValores);

        FloatingActionButton addButton = view.findViewById(R.id.floatingActionButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarTarea();
            }
        });

        return view;
    }

    public void agregarTarea() {
        if (taskValue.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Introduzca una tarea", Toast.LENGTH_SHORT).show();
            return;
        }
        listaValores.add(taskValue.getText().toString());
        adapterValores.notifyDataSetChanged();
        taskValue.setText("");
    }

    public void verificar(final int id) {
        builder.setMessage("Tarea -> " + listaValores.get(id));
        builder.setCancelable(false);
        builder.setPositiveButton("Terminada", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listaValores.remove(id);
                adapterValores.notifyDataSetChanged();
                todo.setAdapter(adapterValores);
                Toast.makeText(requireContext(), "Tarea Terminada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(requireContext(), "Cancelado, Tarea Pendiente", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle("¿Deseas finalizar esta tarea?");
        alert.show();
    }
}