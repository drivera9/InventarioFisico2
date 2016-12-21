package com.example.mono.inventariofisico2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventarioFisicoWbeimar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_fisico_wbeimar);


        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("SELECCIONAR OPCION");
        categories.add("CONSULTAR TODO");
        categories.add("BORRAR UBICACION");
        categories.add("BORRAR PLU");
        categories.add("CONSULTAR PLU");
        categories.add("CONSULTAR UBICACION");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        EditText editPlu = (EditText) findViewById(R.id.editPlu);

        System.out.println("PASOOOO");


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void consultarTodo() throws IOException, SQLException {
        DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
        dataBaseHelper.createDataBase();
        dataBaseHelper.openDataBase();
        final ArrayList arrayList = dataBaseHelper.consultarTodo();

        dataBaseHelper.close();

        String[] arrayGrid = new String [arrayList.size()];

        for (int i = 0;i<arrayList.size();i++){
            arrayGrid[i] = arrayList.get(i).toString().trim();
        }

        GridView gridview = (GridView) findViewById(R.id.gridView);// crear el
        // gridview a partir del elemento del xml gridview

        gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid, "grd"));// con setAdapter se llena


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });


    }
}
