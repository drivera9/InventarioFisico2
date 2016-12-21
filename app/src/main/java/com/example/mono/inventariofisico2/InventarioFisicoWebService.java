package com.example.mono.inventariofisico2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InventarioFisicoWebService extends Activity {


    GridView grid;
    GridView gridTitulos;
    private final String[] items = new String[]{"NUM" ,"UBIC" ,"REF" ,"PLU" ,"DESC"};
    String ip = "";

    EditText editText2;
    String dirFoto;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario_fisico);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mProgressDialog= new ProgressDialog(InventarioFisicoWebService.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Subiendo imagen...");

        ip = "10.0.10.110";

        gridTitulos = (GridView) findViewById(R.id.gridView2);
        CustomGridViewAdapter gridViewAdapter = new CustomGridViewAdapter(InventarioFisicoWebService.this, items,"titulos");
        gridTitulos.setAdapter(gridViewAdapter);

        final EditText editText = (EditText) findViewById(R.id.editText_plu);
        editText2 = (EditText) findViewById(R.id.editText_ubic);
        final EditText editText3 = (EditText) findViewById(R.id.editText_cant);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_opciones);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner_opcionesvisibles);

        ((TextView) findViewById(R.id.textView_cant)).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.textView_grp)).setVisibility(View.INVISIBLE);
        ((EditText) findViewById(R.id.editText_cant)).setVisibility(View.INVISIBLE);
        ((EditText) findViewById(R.id.editText_grp)).setVisibility(View.INVISIBLE);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(InventarioFisicoWebService.this, R.array.plu_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter((SpinnerAdapter) arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int n, long id) {
                if (parent.getItemAtPosition(n).toString().equals((Object) "PLU")) {
                    ((TextView) InventarioFisicoWebService.this.findViewById(R.id.textView_cant)).setVisibility(View.INVISIBLE);
                    ((EditText) InventarioFisicoWebService.this.findViewById(R.id.editText_cant)).setVisibility(View.INVISIBLE);
                    ((TextView) InventarioFisicoWebService.this.findViewById(R.id.textView_grp)).setVisibility(View.INVISIBLE);
                    ((EditText) InventarioFisicoWebService.this.findViewById(R.id.editText_grp)).setVisibility(View.INVISIBLE);
                }
                if (parent.getItemAtPosition(n).toString().equals((Object) "PLU + CANT")) {
                    ((TextView) InventarioFisicoWebService.this.findViewById(R.id.textView_cant)).setVisibility(View.VISIBLE);
                    ((EditText) InventarioFisicoWebService.this.findViewById(R.id.editText_cant)).setVisibility(View.VISIBLE);
                    ((TextView) InventarioFisicoWebService.this.findViewById(R.id.textView_grp)).setVisibility(View.INVISIBLE);
                    ((EditText) InventarioFisicoWebService.this.findViewById(R.id.editText_grp)).setVisibility(View.INVISIBLE);
                }
                if (parent.getItemAtPosition(n).toString().equals((Object) "PLU + CANT + GRP")) {
                    ((TextView) InventarioFisicoWebService.this.findViewById(R.id.textView_cant)).setVisibility(View.VISIBLE);
                    ((EditText) InventarioFisicoWebService.this.findViewById(R.id.editText_cant)).setVisibility(View.VISIBLE);
                    ((TextView) InventarioFisicoWebService.this.findViewById(R.id.textView_grp)).setVisibility(View.VISIBLE);
                    ((EditText) InventarioFisicoWebService.this.findViewById(R.id.editText_grp)).setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView adapterView) {
            }
        });

        ArrayAdapter arrayAdapter2 = ArrayAdapter.createFromResource(InventarioFisicoWebService.this, R.array.opciones_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter((SpinnerAdapter) arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int n, long id) {

                if (parent.getItemAtPosition(n).toString().equals((Object) "Consultar todo")) {
                        consultarTodo();
                    spinner2.setSelection(0);
                }
                if (parent.getItemAtPosition(n).toString().equals((Object) "Consultar PLU")) {

                        consultarPLU();

                    spinner2.setSelection(0);
                }
                if (parent.getItemAtPosition(n).toString().equals((Object) "Consultar ubicacion")) {

                        consultarUbic();

                    spinner2.setSelection(0);
                }
                if (parent.getItemAtPosition(n).toString().equals((Object) "Borrar PLU")) {

                        borrarPLU();

                    spinner2.setSelection(0);
                }
                if (parent.getItemAtPosition(n).toString().equals((Object) "Borrar ubicacion")) {

                        borrarUbicacion();

                    spinner2.setSelection(0);
                }
                if (parent.getItemAtPosition(n).toString().equals((Object) "Nuevo producto")) {
                    guardarNuevoProducto();
                }
            }

            public void onNothingSelected(AdapterView adapterView) {
            }

        });

        try {
            editText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        try {
                            mProgressDialog.show();
                            //new ConsultarDatos().execute("");
                            guardarProducto(editText.getText().toString(), Integer.parseInt((String) editText2.getText().toString()), editText3.getText().toString(), 1);
                            Toast.makeText((Context) InventarioFisicoWebService.this, (CharSequence) "se guardo correctamente!", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                            editText.setText((CharSequence) "");
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText((Context) InventarioFisicoWebService.this, (CharSequence) "Hace faltan datos!", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                    return false;
                }
            });
            editText3.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    int n2 = event.getAction();
                    boolean bl = false;
                    if (n2 != 0) return bl;
                    bl = false;
                    if (keyCode != 66) return bl;
                    try {
                        guardarProducto(editText.getText().toString(), Integer.parseInt((String) editText2.getText().toString()), " ", Integer.parseInt((String) editText3.getText().toString()));
                        Toast.makeText((Context) InventarioFisicoWebService.this, (CharSequence) "se guardo correctamente!", Toast.LENGTH_SHORT).show();
                        editText.setText((CharSequence) "");
                        editText3.setText((CharSequence) "");
                        do {
                            return true;
                        } while (true);

                    } catch (Exception ve) {
                        Toast.makeText((Context) InventarioFisicoWebService.this, (CharSequence) "Hace faltan datos!", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            });
            return;
        } catch (Exception var18_19) {
            Toast.makeText(InventarioFisicoWebService.this, (CharSequence) "faltan datos!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public void camara(View v) {
        String nombre;
        nombre = editText2.getText().toString();

        System.out.println("ESTE ES EL NOMBRE -> " + nombre );

        dirFoto =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  + "/CLENDY/";

        System.out.println(dirFoto);

        File fecha = new File(dirFoto);

        fecha.mkdirs();

        String file = dirFoto + nombre +  ".jpg";


        File mi_foto = new File(file);
        try {
            mi_foto.createNewFile();

            //
            Uri uri = Uri.fromFile(mi_foto);
            //Abre la camara para tomar la foto
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Guarda imagen
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            //Retorna a la actividad
            startActivityForResult(cameraIntent, 0);
            //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
            String strPath = dirFoto + nombre +  ".jpg";
            //String strPath = "/storage/E6E7-2A20/METRO/" + nombre;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
        } catch (IOException ex) {
            Log.e("ERROR ", "Error:" + ex);
        }


    }

    public void zoom(View v){

        String nombre;
        nombre = editText2.getText().toString();

        dirFoto =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  + "/CLENDY/";


        String file = dirFoto + nombre +  ".jpg";


        Bundle bundle = new Bundle();
        Intent intent = new Intent((Context) this, (Class) AmpliarImagen.class);
        bundle.putString("nombre", nombre);
        bundle.putString("path", file);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }


    public void upload(View v){
        new ConsultarDatos().execute("");
    }


    class ConsultarDatos extends AsyncTask<String, String, String> {

        String resultado;
        String user;
        String nombre;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
            nombre = editText2.getText().toString();

        }
        @Override
        protected String doInBackground(String... f_url) {
            //10.0.10.252
            String url = "http://" + ip + "/upload.php";
            System.out.println(url);
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(url);

            dirFoto =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  + "/CLENDY/";
            //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
            String imagen =  dirFoto + nombre +  ".jpg";
            System.out.println(imagen + " UPLOAD");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(new File(imagen));
            builder.addPart("uploaded", fileBody);
            System.out.println(getDia());
            builder.addTextBody("dia", "");

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            try{
                httpClient.execute(httpPost);
                httpClient.getConnectionManager().shutdown();
                System.out.println("guardo");
            }catch (ClientProtocolException e){
                System.out.println("no guardo");
                e.printStackTrace();
            }catch (IOException e){
                System.out.println("noo guardo");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            mProgressDialog.dismiss();
        }
    }

    private String getDia() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(new Date());
        return date;
    }

    public void guardarProducto (String plu,int ubic,String cant,int d){

        String url = "http://" + ip + "/guardarPLU.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sUbicacion", String.valueOf(ubic)));
        params.add(new BasicNameValuePair("sPlu", plu));
        params.add(new BasicNameValuePair("sCantidad", cant));
        params.add(new BasicNameValuePair("sEstado", "A"));
        params.add(new BasicNameValuePair("sUsuario", "JUAN"));
        params.add(new BasicNameValuePair("sPeriodo", "1"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
    }

    public void consultarTodo(){

    }

    public void consultarPLU(){

    }

    public void consultarUbic(){

    }

    public void borrarPLU(){

    }

    public void borrarUbicacion(){

    }

    public void guardarNuevoProducto(){

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
