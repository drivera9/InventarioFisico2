package com.example.mono.inventariofisico2;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.Context
 *  android.os.Bundle
 *  android.text.Editable
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 *  android.widget.EditText
 *  java.io.IOException
 *  java.lang.String
 *  java.sql.SQLException
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.sql.SQLException;

/*
 * Failed to analyse overrides
 */
public class CustomDialogClass
        extends Dialog
        implements View.OnClickListener {
    public int _id;
    public Activity c;
    public Dialog d;
    public Button ok;
    public int posicion;
    public String plu;
    public String ref;
    public String ubic;

    public CustomDialogClass(Activity activity, int n, int n2,String plu,String ref,String ubic) {
        super((Context)activity);
        this.c = activity;
        this._id = n;
        this.posicion = n2;
        this.plu = plu;
        this.ref = ref;
        this.ubic = ubic;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void onClick(View v) {
        EditText texto = (EditText) findViewById(R.id.editText_texto);
        switch (v.getId()) {
            case R.id.btn_yes:
                DataBaseHelper dataBaseHelper = new DataBaseHelper(this.getContext());
                try {
                    dataBaseHelper.createDataBase();
                }
                catch (IOException var4_4) {
                    var4_4.printStackTrace();
                }
                try {
                    dataBaseHelper.openDataBase();
                }
                catch (SQLException var5_5) {
                    var5_5.printStackTrace();
                }
                dataBaseHelper.update2(texto.getText().toString(),plu,ref,ubic);
                this.dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }



    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.custom_dialog);
        this.ok = (Button)this.findViewById(R.id.btn_yes);
        this.ok.setOnClickListener((View.OnClickListener)this);
    }
}

