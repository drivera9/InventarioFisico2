package com.example.mono.inventariofisico2;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.app.AlertDialog
 *  android.app.AlertDialog$Builder
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.KeyEvent
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$OnKeyListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.ArrayAdapter
 *  android.widget.EditText
 *  android.widget.GridView
 *  android.widget.ListAdapter
 *  android.widget.Spinner
 *  android.widget.SpinnerAdapter
 *  android.widget.TextView
 *  android.widget.Toast
 *  java.io.IOException
 *  java.io.PrintStream
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.sql.SQLException
 *  java.util.ArrayList
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Failed to analyse overrides
 */
public class InventarioFisico
        extends Activity implements AdapterView.OnItemSelectedListener  {
    GridView grid;
    GridView gridTitulos;
    String PLU = "";
    EditText edittext ;
    EditText editUbic;
    String plu = "";
    ArrayList resultado;
    boolean estaPLU = false;
    String url = "";
    String ip = "";
    List<NameValuePair> params;
    private final String[] items = new String[]{"NUM" ,"UBIC" ,"REF" ,"PLU" ,"DESC"};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_inventario_fisico_wbeimar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText Ip = (EditText) findViewById(R.id.editIp);
        ip = Ip.getText().toString().trim();

        final CheckBox checkLocal = (CheckBox) findViewById(R.id.checkLocal);
        checkLocal.setChecked(true);

        final CheckBox checkRemoto = (CheckBox) findViewById(R.id.checkRemoto);
        checkRemoto.setChecked(false);

        if (checkLocal.isChecked()){
            checkRemoto.setChecked(false);
        }else{
            if (checkRemoto.isChecked()){
                checkLocal.setChecked(false);
            }
        }

        checkLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (checkLocal.isChecked()){
                    checkRemoto.setChecked(false);
                }
            }
        });

        checkRemoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (checkRemoto.isChecked()){
                    checkLocal.setChecked(false);
                }
            }
        });

        edittext = (EditText) findViewById(R.id.editPlu);
        editUbic = (EditText) findViewById(R.id.editUbic);
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


        String[] items = new String[]{"DESC" ,"PLU" ,"UBIC" ,"CANT" ,"EST"};

        GridView gridview = (GridView) findViewById(R.id.gridViewTitulos);// crear el
        // gridview a partir del elemento del xml gridview

        gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), items, "grid"));// con setAdapter se llena


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });



        editUbic.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //editUbic.setText(editUbic.getText().toString().trim());
                    new GuardarMovimientoUbic().execute("");
                }

            return false;

        }

    });

        final EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    if (checkLocal.isChecked()) {
                        PLU = edittext.getText().toString().trim();
                        ArrayList resultado = new ArrayList();

                        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getApplicationContext(), "wms", null, 1);
                        SQLiteDatabase bd = admin.getWritableDatabase();
                        Cursor fila = bd.rawQuery(
                                "SELECT cod_plu  FROM tra_movimiento WHERE cod_plu = '" + edittext.getText().toString() + "'" + "", null);

                        if (fila.moveToFirst()) {
                            do {
                                resultado.add(fila.getString(0));
                            } while (fila.moveToNext());
                        }
                        bd.close();

                        if (resultado.size() > 0) {

                            if (resultado.get(0).toString().trim().equals(edittext.getText().toString().trim())) {
                                new GuardarMovimiento().execute("");
                            }
                        } else {
                            new AlertDialog.Builder(InventarioFisico.this)
                                    .setTitle("CODIGO NUEVO")
                                    .setMessage("ESTE ES UN CODIGO NUEVO")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            guardarProducto();
                                            consultarTodo();
                                            edittext.invalidate();
                                            edittext.setText("");
                                            edittext.requestFocus();
                                            edittext.invalidate();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }

                    }else{

                        new GuardarMovimientoWEB().execute("");


                    }
                }

                return false;

            }

        });



        //edittext.setInputType(InputType.TYPE_NULL);
    }

    public void exportar (View v){
        CSVWriter writer = null;
        try
        {
            writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/myfile.csv"), ',');

            String[] entries = ("DESCRIPCION" + "#" + "PLU" + "#" + "UBICACION" + "#" +
                    "CANTIDAD").split("#");

            writer.writeNext(entries);

            ArrayList resultado = new ArrayList();

            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                    "wms", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery(
                    "SELECT descripcion,cod_plu,cod_ubicacion,can_unidades  FROM tra_movimiento", null);

            if (fila.moveToFirst()) {
                do {
                    resultado.add(fila.getString(0));
                    resultado.add(fila.getString(1));
                    resultado.add(fila.getString(2));
                    resultado.add(fila.getString(3));
                } while (fila.moveToNext());
            }


            bd.close();

            int j = 0;
            for (int i = 0;i<(resultado.size()/4);i++){
                entries = (resultado.get(j).toString().trim() + "#" + resultado.get(j+1).toString().trim() + "#" +
                        resultado.get(j+2).toString().trim() + "#" + resultado.get(j+3).toString().trim()  ).split("#");

                writer.writeNext(entries);
                j = j+4;
            }
            // array of your values

            writer.close();

            Toast.makeText(InventarioFisico.this, "Se ha exportado exitosamente!", Toast.LENGTH_SHORT).show();

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "INFORME");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "INFORME");
            File root = Environment.getExternalStorageDirectory();
            String pathToMyAttachedFile = "myfile.csv";
            File file = new File(root, pathToMyAttachedFile);
            if (!file.exists() || !file.canRead()) {
                return;
            }
            Uri uri = Uri.fromFile(file);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    class GuardarMovimiento extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute(){


        }
        @Override
        protected String doInBackground(String... f_url) {
            ArrayList<Boolean> atributosBool = new ArrayList<>();

            guardarProducto();

            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            edittext.invalidate();
            edittext.setText("");
            edittext.requestFocus();
            edittext.invalidate();
            consultarTodo();
        }
    }

    class GuardarMovimientoUbic extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute(){


        }
        @Override
        protected String doInBackground(String... f_url) {
            ArrayList<Boolean> atributosBool = new ArrayList<>();



            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            edittext.invalidate();
            edittext.setText("");
            edittext.requestFocus();
            edittext.invalidate();
            consultarTodo();
        }
    }

    class GuardarMovimientoWEB extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute(){

            EditText editPlu = (EditText) findViewById(R.id.editPlu);

            resultado = new ArrayList();
            plu = editPlu.getText().toString();

        }
        @Override
        protected String doInBackground(String... f_url) {
            ArrayList<Boolean> atributosBool = new ArrayList<>();

            url = "http://" + ip + "/consultarGeneralInv.php";

            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sPlu", plu));
            params.add(new BasicNameValuePair("sParametro", "consultarPlu"));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);

            try {
                JSONArray jArray = new JSONArray(resultServer);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    resultado.add((json.getString("cod_plu")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                resultado.add("");
            }

            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {

            if (resultado.get(0).toString().trim().equals("null") || resultado.get(0).toString().trim().equals("")) {
                estaPLU = false;
            }else{
                estaPLU = true;
            }

            if (estaPLU) {

                if (resultado.get(0).toString().trim().equals(edittext.getText().toString().trim())) {
                    guardarProductoWEB();
                    consultarTodoWEB();
                    edittext.invalidate();
                    edittext.setText("");
                    edittext.requestFocus();
                    edittext.invalidate();
                }
            } else {
                new AlertDialog.Builder(InventarioFisico.this)
                        .setTitle("CODIGO NUEVO")
                        .setMessage("ESTE ES UN CODIGO NUEVO")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                guardarProductoWEB();
                                consultarTodoWEB();
                                edittext.invalidate();
                                edittext.setText("");
                                edittext.requestFocus();
                                edittext.invalidate();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        }
    }

    public void guardar(View v){
        guardarProducto();
        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        EditText Ip = (EditText) findViewById(R.id.editIp);
        ip = Ip.getText().toString().trim();

        String item = parent.getItemAtPosition(position).toString();

        CheckBox checkLocal = (CheckBox) findViewById(R.id.checkLocal);

        if (checkLocal.isChecked()) {

            if (item.equals("CONSULTAR PLU")) {
                consultarPLU();
            }

            if (item.equals("CONSULTAR TODO")) {
                consultarTodo();
            }

            if (item.equals("CONSULTAR UBICACION")) {
                consultarUbicacion();
            }

            if (item.equals("BORRAR PLU")) {
                borrarPLU();
            }

            if (item.equals("BORRAR UBICACION")) {
                borrarUbic();
            }

            parent.setSelection(0);

        }else{

            if (item.equals("CONSULTAR PLU")) {
                consultarPLUWEB();
            }

            if (item.equals("CONSULTAR TODO")) {
                consultarTodoWEB();
            }

            if (item.equals("CONSULTAR UBICACION")) {
                consultarUbicacionWEB();
            }

            if (item.equals("BORRAR PLU")) {
                borrarPLUWEB();
            }

            if (item.equals("BORRAR UBICACION")) {
                borrarUbicWEB();
            }

            parent.setSelection(0);
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void consultarTodo() {

        ArrayList resultado = new ArrayList();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "wms", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT descripcion,cod_plu,cod_ubicacion,can_unidades,ind_estado  FROM tra_movimiento", null);
        String total = "";
        total = String.valueOf(fila.getCount());

        if (fila.moveToFirst()) {
            do {
                resultado.add(fila.getString(0));
                resultado.add(fila.getString(1));
                resultado.add(fila.getString(2));
                resultado.add(fila.getString(3));
                resultado.add(fila.getString(4));
            } while (fila.moveToNext());
        }


        bd.close();

        String[] arrayGrid = new String [resultado.size()];

        for (int i = 0;i<resultado.size();i++){
            arrayGrid[i] = resultado.get(i).toString().trim();
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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = total;
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    public void consultarTodoWEB() {

        ArrayList resultado = new ArrayList();
        int total = 0;

        url = "http://" + ip + "/consultarGeneralInv.php";

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "consultarTodo"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                resultado.add((json.getString("descripcion")));
                resultado.add((json.getString("cod_plu")));
                resultado.add((json.getString("cod_ubicacion")));
                resultado.add((json.getString("can_unidades")));
                resultado.add((json.getString("ind_estado")));
                total++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }


        String[] arrayGrid = new String [resultado.size()];

        for (int i = 0;i<resultado.size();i++){
            arrayGrid[i] = resultado.get(i).toString().trim();
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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = String.valueOf(total);
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    public void consultarPLU() {

        EditText editPlu = (EditText) findViewById(R.id.editPlu);
        ArrayList resultado = new ArrayList();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "wms", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT descripcion,cod_plu,cod_ubicacion,can_unidades,ind_estado  " +
                        "FROM tra_movimiento WHERE cod_plu = '" + editPlu.getText().toString() + "'" + "", null);

        String total = "";
        total = String.valueOf(fila.getCount());

        if (fila.moveToFirst()) {
            do {
                resultado.add(fila.getString(0));
                resultado.add(fila.getString(1));
                resultado.add(fila.getString(2));
                resultado.add(fila.getString(3));
                resultado.add(fila.getString(4));
            } while (fila.moveToNext());
        }

        bd.close();

        String[] arrayGrid = new String [resultado.size()];

        for (int i = 0;i<resultado.size();i++){
            arrayGrid[i] = resultado.get(i).toString().trim();
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


        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = total;
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();
    }

    public void consultarPLUWEB() {

        EditText editPlu = (EditText) findViewById(R.id.editPlu);

        ArrayList resultado = new ArrayList();
        int total = 0;

        url = "http://" + ip + "/consultarGeneralInv.php";

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sPlu", editPlu.getText().toString()));
        params.add(new BasicNameValuePair("sParametro", "consultarPlu"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                resultado.add((json.getString("descripcion")));
                resultado.add((json.getString("cod_plu")));
                resultado.add((json.getString("cod_ubicacion")));
                resultado.add((json.getString("can_unidades")));
                resultado.add((json.getString("ind_estado")));
                total++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

        String[] arrayGrid = new String [resultado.size()];

        for (int i = 0;i<resultado.size();i++){
            arrayGrid[i] = resultado.get(i).toString().trim();
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


        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = String.valueOf(total);
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();
    }

    public void consultarUbicacion() {

        EditText editUbic = (EditText) findViewById(R.id.editUbic);
        ArrayList resultado = new ArrayList();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "wms", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "SELECT descripcion,cod_plu,cod_ubicacion,can_unidades,ind_estado  " +
                        "FROM tra_movimiento WHERE cod_ubicacion = '" + editUbic.getText().toString() + "'" + "", null);

        String total = "";
        total = String.valueOf(fila.getCount());

        if (fila.moveToFirst()) {
            do {
                resultado.add(fila.getString(0));
                resultado.add(fila.getString(1));
                resultado.add(fila.getString(2));
                resultado.add(fila.getString(3));
                resultado.add(fila.getString(4));
            } while (fila.moveToNext());
        }

        bd.close();

        String[] arrayGrid = new String [resultado.size()];

        for (int i = 0;i<resultado.size();i++){
            arrayGrid[i] = resultado.get(i).toString().trim();
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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = total;
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    public void consultarUbicacionWEB() {

        EditText editUbic = (EditText) findViewById(R.id.editUbic);

        ArrayList resultado = new ArrayList();
        int total = 0;

        url = "http://" + ip + "/consultarGeneralInv.php";

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sUbic", editUbic.getText().toString()));
        params.add(new BasicNameValuePair("sParametro", "consultarUbic"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                resultado.add((json.getString("descripcion")));
                resultado.add((json.getString("cod_plu")));
                resultado.add((json.getString("cod_ubicacion")));
                resultado.add((json.getString("can_unidades")));
                resultado.add((json.getString("ind_estado")));
                total++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

        String[] arrayGrid = new String [resultado.size()];

        for (int i = 0;i<resultado.size();i++){
            arrayGrid[i] = resultado.get(i).toString().trim();
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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = String.valueOf(total);
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    public void borrarPLU(){
        EditText editPlu = (EditText) findViewById(R.id.editPlu);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "wms", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod= editPlu.getText().toString();
        int cant = bd.delete("tra_movimiento", "cod_plu =" + cod, null);
        bd.close();

        String[] arrayGrid = new String [5];
        arrayGrid[0] = "";
        arrayGrid[1] = "";
        arrayGrid[2] = "";
        arrayGrid[3] = "";
        arrayGrid[4] = "";

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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = "0";
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();
    }

    public void borrarPLUWEB(){

        EditText editPlu = (EditText) findViewById(R.id.editPlu);

        ArrayList resultado = new ArrayList();
        int total = 0;

        url = "http://" + ip + "/guardarMovimientoInv.php";

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sPlu", editPlu.getText().toString()));
        params.add(new BasicNameValuePair("sParametro", "borrarPlu"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        String[] arrayGrid = new String [5];
        arrayGrid[0] = "";
        arrayGrid[1] = "";
        arrayGrid[2] = "";
        arrayGrid[3] = "";
        arrayGrid[4] = "";

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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = "0";
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();
    }

    public void borrarUbic(){
        EditText editPlu = (EditText) findViewById(R.id.editUbic);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "wms", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod= editPlu.getText().toString();
        int cant = bd.delete("tra_movimiento", "cod_ubicacion =" + cod, null);
        bd.close();

        String[] arrayGrid = new String [5];
        arrayGrid[0] = "";
        arrayGrid[1] = "";
        arrayGrid[2] = "";
        arrayGrid[3] = "";
        arrayGrid[4] = "";

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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = "0";
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    public void borrarUbicWEB(){
        EditText editUbic = (EditText) findViewById(R.id.editUbic);

        ArrayList resultado = new ArrayList();
        int total = 0;

        url = "http://" + ip + "/guardarMovimientoInv.php";

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sUbic", editUbic.getText().toString()));
        params.add(new BasicNameValuePair("sParametro", "borrarUbic"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        String[] arrayGrid = new String [5];
        arrayGrid[0] = "";
        arrayGrid[1] = "";
        arrayGrid[2] = "";
        arrayGrid[3] = "";
        arrayGrid[4] = "";

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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = "0";
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    public void borrar(){
        EditText editPlu = (EditText) findViewById(R.id.editUbic);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "wms", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String cod= editPlu.getText().toString();
        int cant = bd.delete("tra_movimiento", "", null);
        bd.close();

        String[] arrayGrid = new String [5];
        arrayGrid[0] = "";
        arrayGrid[1] = "";
        arrayGrid[2] = "";
        arrayGrid[3] = "";
        arrayGrid[4] = "";

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

        String[] arrayGrid2 = new String [5];
        arrayGrid2[0] = "TOTAL";
        arrayGrid2[1] = "0";
        arrayGrid2[2] = "";
        arrayGrid2[3] = "";
        arrayGrid2[4] = "";

        GridView gridviewTotales = (GridView) findViewById(R.id.gridViewTotales);// crear el
        // gridview a partir del elemento del xml gridview

        gridviewTotales.setAdapter(new CustomGridViewAdapter(getApplicationContext(), arrayGrid2, "grid"));// con setAdapter se llena


        gridviewTotales.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // Toast para mostrar un mensaje. Escribe el nombre de tu clase
                // si no la llamaste MainActivity.


            }
        });

        EditText edittext = (EditText) findViewById(R.id.editPlu);

        edittext.requestFocus();

    }

    public void guardarProducto(){
        EditText editPlu = (EditText) findViewById(R.id.editPlu);
        EditText editUbic = (EditText) findViewById(R.id.editUbic);

        String plu = "";
        String ubic = "";
        try {
            ubic = editUbic.getText().toString();
            plu = editPlu.getText().toString();
        }catch (Exception e){
            e.printStackTrace();
        }


        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "wms", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("cod_ubicacion", ubic.trim());
        registro.put("cod_plu",PLU);
        registro.put("can_unidades", "1");
        registro.put("ind_estado", "A");
        registro.put("descripcion", "prueba");
        bd.insert("tra_movimiento", null, registro);
        bd.close();


    }

    public void guardarProductoWEB(){
        EditText editPlu = (EditText) findViewById(R.id.editPlu);
        EditText editUbic = (EditText) findViewById(R.id.editUbic);

        String plu = "";
        String ubic = "";
        try {
            ubic = editUbic.getText().toString().trim();
            plu = editPlu.getText().toString().trim();
        }catch (Exception e){
            e.printStackTrace();
        }


        url = "http://" + ip + "/guardarMovimientoInv.php";

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sPlu",plu));
        params.add(new BasicNameValuePair("sUbic", ubic));
        params.add(new BasicNameValuePair("sCant", "1"));
        params.add(new BasicNameValuePair("sDesc", "prueba"));
        params.add(new BasicNameValuePair("sParametro", "guardarMovimiento"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

    }

    public void borrar(View v){
        borrar();
    }



    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131296441) {
            return true;
        }
        return onOptionsItemSelected(menuItem);
    }



    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }


}




