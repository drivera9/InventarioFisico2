package com.example.mono.inventariofisico2;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.Menu
 *  android.view.MenuInflater
 *  android.view.MenuItem
 *  android.view.View
 *  android.widget.EditText
 *  android.widget.Toast
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.String
 *  java.sql.SQLException
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;

/*
 * Failed to analyse overrides
 */
public class GuardarProducto
        extends Activity {
    public void guardar(View view) throws IOException, SQLException {
        try {
            EditText plu = (EditText)this.findViewById(R.id.editText_plu);
            EditText ubic = (EditText)this.findViewById(R.id.editText_ubic);
            EditText desc = (EditText)this.findViewById(R.id.editText_desc);
            EditText ref = (EditText)this.findViewById(R.id.editText_ref);
            EditText cant = (EditText)this.findViewById(R.id.editText_cant);
            EditText costo = (EditText)this.findViewById(R.id.editText_costo);

            int n = Integer.parseInt((String) plu.getText().toString());
            String des = desc.getText().toString();
            int n2 = Integer.parseInt((String) ubic.getText().toString());
            int n3 = Integer.parseInt((String) ref.getText().toString());
            int n4 = Integer.parseInt((String) cant.getText().toString());
            int n5 = Integer.parseInt((String) costo.getText().toString());
            DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            dataBaseHelper.guardarProductoPLU(n, n2, n3, n4, des, n5);
            dataBaseHelper.close();
            Toast.makeText((Context) this, (CharSequence) "Se guardo correctamente", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        catch (Exception var2_15) {
            Toast.makeText((Context) this, (CharSequence) "No se pudo guardar!!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.nuevo_producto);
        EditText plu = (EditText)this.findViewById(R.id.editText_plu);
        EditText ubic = (EditText)this.findViewById(R.id.editText_ubic);
        Bundle bundle2 = this.getIntent().getExtras();
        String string = bundle2.getString("plu");
        String string2 = bundle2.getString("ubic");
        plu.setText((CharSequence)string);
        ubic.setText((CharSequence)string2);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        //this.getMenuInflater().inflate(2131230724, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 2131296441) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}

