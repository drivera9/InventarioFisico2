package com.example.mono.inventariofisico2;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.database.sqlite.SQLiteException
 *  android.database.sqlite.SQLiteOpenHelper
 *  java.io.FileOutputStream
 *  java.io.IOException
 *  java.io.InputStream
 *  java.io.PrintStream
 *  java.lang.Error
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.sql.SQLException
 *  java.util.ArrayList
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * Failed to analyse overrides
 */
public class DataBaseHelper
        extends SQLiteOpenHelper {
    private static String DB_NAME;
    private static String DB_PATH;
    protected static final String TAG = "DataAdapter";
    private Context myContext;
    private SQLiteDatabase myDataBase;

    static {
        DB_PATH = "/data/data/com.example.mono.inventariofisico2/databases/";
        DB_NAME = "wms";
    }

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n, int n2) {
    }

    private void copyDataBase() throws IOException {
        int n;
        InputStream inputStream = this.myContext.getAssets().open(DB_NAME);
        FileOutputStream fileOutputStream = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] arrby = new byte[1024];
        while ((n = inputStream.read(arrby)) > 0) {
            fileOutputStream.write(arrby, 0, n);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
    }

    public String[][] consultarProceso(String usuario, String empresa, String sucursal, String proceso,int orden){

        String string = "SELECT *  FROM tra_dcaptura  WHERE cod_usuario = '" + usuario +  "' AND cod_empresa = '" + empresa + "' AND ind_estado = 'A'";
        System.out.println(string);
        Cursor cursor = this.myDataBase.rawQuery(string, null);
        String[][] general = new String[cursor.getCount()][18];

        //general[0][0] = cursor.getCount()+ "";
        int j = 0;
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 17; ++i) {
                    if (cursor.isNull(i)) {
                        general[j][i] = "";
                        continue;
                    }
                    general[j][i] = cursor.getString(i);
                }
                j++;
            } while (cursor.moveToNext());
        }
        return general;
    }

    public void borrarPLU(String string) {
        this.myDataBase.delete("des_invfisico_tmp", "cod_plu = '" + string + "'" + "", null);
    }

    public void borrarUbic(String string) {
        this.myDataBase.delete("des_invfisico_tmp", "cod_ubicacion = '" + string + "'" + "", null);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public boolean checkDataBase() {
        SQLiteDatabase sQLiteDatabase;
        try {
            SQLiteDatabase sQLiteDatabase2;
            sQLiteDatabase = sQLiteDatabase2 = SQLiteDatabase.openDatabase((String) (DB_PATH + DB_NAME), (SQLiteDatabase.CursorFactory) null, (int) 0);
        }
        catch (SQLiteException var1_4) {
            return false;
        }
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
        boolean bl = false;
        if (sQLiteDatabase == null) return bl;
        return true;
    }

    public void close() {
        DataBaseHelper dataBaseHelper = this;
        synchronized (dataBaseHelper) {
            if (this.myDataBase != null) {
                this.myDataBase.close();
            }
            super.close();
            return;
        }
    }

    public void guardarProceso(String cod_empresa,String cod_sucursal,String cod_usuario,
                               String cod_proceso,String cod_atributo,String cod_valor,String num_orden){
        ContentValues contentValues = new ContentValues();
        contentValues.put("cod_empresa", cod_empresa);
        contentValues.put("cod_sucursal", cod_sucursal);
        contentValues.put("cod_usuario", cod_usuario);
        contentValues.put("cod_proceso", cod_proceso );
        contentValues.put("cod_atributo", cod_atributo );
        contentValues.put("cod_valor", cod_valor);
        contentValues.put("num_orden", num_orden );

        this.myDataBase.insert("tra_movimientos", null, contentValues);
    }

    /*
     * Enabled aggressive block sorting
     */
    public ArrayList consultarPLU(String string) {
        String string2 = "SELECT des_invfisico_tmp._id, des_invfisico_tmp.cod_ubicacion,des_invfisico_tmp.cod_referencia,des_invfisico_tmp.cod_plu  FROM des_invfisico_tmp WHERE des_invfisico_tmp.cod_plu = '" + string + "'" + "";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 3; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    /*
     * Enabled aggressive block sorting
     */
    public ArrayList consultarTodo() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT des_invfisico_tmp._id,des_invfisico_tmp.cod_ubicacion,des_invfisico_tmp.cod_plu  FROM des_invfisico_tmp ", null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 3; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }

        return arrayList;
    }
    /*
     * Enabled aggressive block sorting
     */
    public ArrayList consultarUbic(String string) {
        String string2 = "SELECT des_invfisico_tmp._id, des_invfisico_tmp.cod_ubicacion,des_invfisico_tmp.cod_referencia,des_invfisico_tmp.cod_plu,plu.descripcion  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE des_invfisico_tmp.cod_ubicacion  = '" + string + "'" + "";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i <= 4; ++i) {
                    if (cursor.isNull(i)) {
                        arrayList.add("");
                        continue;
                    }
                    arrayList.add(cursor.getString(i));
                }
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public void createDataBase() throws IOException {
        if (this.checkDataBase()) {
            return;
        }
        this.getWritableDatabase();
        try {
            this.copyDataBase();
            return;
        }
        catch (IOException var2_1) {
            throw new Error("Error copiando Base de Datos");
        }
    }
    public ArrayList getConsultas(String string) {
        String string2 = "SELECT sysmod.desc_opc FROM security_groupings,security_users,sysmod,syspan,sysper  WHERE ( sysmod.codi_pan =syspan.codi_pan ) and  ( sysmod.codi_emp = sysper.codi_emp ) and  ( sysmod.codi_mod = sysper.codi_opc ) and   ( security_groupings.group_name = security_users.name ) and  ( sysper.codi_are = security_users.name)  and  ( security_users.name = '" + string + "'" + ")";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getContrasenas() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT password FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getContrasenasProcesos(String usuario) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT cod_clave from tra_captura WHERE nom_usuario = '" + usuario + "'" , null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getProcesos(String usuario) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT cod_proceso from tra_captura WHERE nom_usuario = '" + usuario + "'" , null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        for (int i = 0;i<arrayList.size();i++){
            System.out.println(arrayList.get(i)+ usuario);
        }
        return arrayList;
    }

    public ArrayList getRombos(String empresa,String sucursal) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT val_recurso from tra_recursos WHERE ind_estado = " +
                "'A' AND cod_empresa = '" + empresa +"' AND cod_sucursal = '" + sucursal + "'" , null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public ArrayList getOpciones() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT user_type FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        for (int i = 0;i<arrayList.size();i++){
            System.out.println(arrayList.get(i));
        }
        return arrayList;
    }

    public void updateNit(String cedula, String nombre1, String nombre2, String apellido1, String apellido2){
        ContentValues contentValues = new ContentValues();
        contentValues.put("num_rombo", "");
        contentValues.put("cod_placa", "");
        contentValues.put("num_nit", cedula);
        contentValues.put("nom_apellido1", apellido1);
        contentValues.put("nom_apellido2", apellido2);
        contentValues.put("nom_nombre1", nombre1);
        contentValues.put("nom_nombre2", nombre2);
        contentValues.put("fec_nacimiento", "");
        contentValues.put("ind_sexo", "");

        this.myDataBase.insert("tra_nit", null, contentValues);
    }

    public ArrayList getUsuarios() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT name FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getVista() {
        Cursor cursor = this.myDataBase.rawQuery("SELECT tipo_vista FROM security_users", null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList getVista2(String string) {
        String string2 = "SELECT sysmod.desc_mod FROM security_groupings,security_users,sysmod,syspan,sysper  WHERE ( sysmod.codi_pan =syspan.codi_pan ) and  ( sysmod.codi_emp = sysper.codi_emp ) and  ( sysmod.codi_mod = sysper.codi_mod ) and   ( security_groupings.group_name = security_users.name ) and  ( sysper.codi_are = security_users.name)  and  ( security_users.name = '" + string + "'" + ")";
        Cursor cursor = this.myDataBase.rawQuery(string2, null);
        ArrayList arrayList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public void guardarProducto(String plu, int ubicacion, int ref, int cant, String string) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cod_empresa", "");
        contentValues.put("ser_invcon", "");
        contentValues.put("cod_tercero", "");
        contentValues.put("tip_tercero", "");
        contentValues.put("cod_plu", plu);
        contentValues.put("cod_proveedoref", "");
        contentValues.put("cod_referencia", ref);
        contentValues.put("ind_calidad", "");
        contentValues.put("num_tiquete", "");
        contentValues.put("cod_ubicacion", ubicacion);
        contentValues.put("can_unidades", cant);
        contentValues.put("cod_usuario", "");
        contentValues.put("ind_estado", "");
        contentValues.put("descripcion", string);
        this.myDataBase.insert("des_invfisico_tmp", null, contentValues);
    }
    public void guardarProductoPLU(int plu, int ubic, int ref, int cant, String string, int costo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("empresa", "");
        contentValues.put("sucursal", "");
        contentValues.put("cod_plu", plu);
        contentValues.put("cod_referencia", ref);
        contentValues.put("cant", cant);
        contentValues.put("costo", costo);
        contentValues.put("precio", "");
        contentValues.put("estado", "");
        contentValues.put("cod_ubicacion", ubic);
        contentValues.put("descripcion", string);
        this.myDataBase.insert("plu", null, contentValues);

    }
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */

    public void openDataBase() throws SQLException {
        try {
            this.createDataBase();
        }
        catch (IOException var1_1) {
            throw new Error("Ha sido imposible crear la Base de Datos");
        }
        this.myDataBase = SQLiteDatabase.openDatabase((String) (DB_PATH + DB_NAME), (SQLiteDatabase.CursorFactory) null, (int) 0);
    }

    public void update2(String string,String plu,String ref, String ubic) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("empresa", "");
        contentValues.put("sucursal", "");
        contentValues.put("cod_plu", plu);
        contentValues.put("cod_referencia", ref);
        contentValues.put("cant", "");
        contentValues.put("costo", "");
        contentValues.put("precio", "");
        contentValues.put("estado", "");
        contentValues.put("cod_ubicacion", ubic);
        contentValues.put("descripcion", string);
        this.myDataBase.insert("plu", null, contentValues);
    }

    public Cursor contar(String string) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT " + string + "  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE not " + string + " = '' ", null);

        return cursor;
    }

    public Cursor contarPLU(String string , String plu) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT  " + string + "  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE des_invfisico_tmp.cod_plu = " + plu + "", null);

        return cursor;
    }

    public Cursor contarUbic(String string , String ubic) {
        Cursor cursor = this.myDataBase.rawQuery("SELECT " + string + "  FROM des_invfisico_tmp LEFT OUTER JOIN plu ON plu.cod_plu = des_invfisico_tmp.cod_plu WHERE des_invfisico_tmp.cod_ubicacion  = " + ubic + "  AND not " + string + " = ''" , null);
        return cursor;
    }

    public void update(String string, int n, int n2) {
        ContentValues contentValues = new ContentValues();
        switch (n2) {
            default: {
                return;
            }
            case 1: {
                String string2 = "_id=" + n;
                contentValues.put("cod_ubicacion", string);
                this.myDataBase.update("des_invfisico_tmp", contentValues, string2, null);
                return;
            }
            case 2: {
                String string3 = "_id=" + n;
                contentValues.put("cod_referencia", string);
                this.myDataBase.update("des_invfisico_tmp", contentValues, string3, null);
                return;
            }
            case 3: {
                String string4 = "_id=" + n;
                contentValues.put("cod_plu", string);
                this.myDataBase.update("des_invfisico_tmp", contentValues, string4, null);
                return;
            }
            case 4:
        }
        String string5 = "_id=" + n;
        contentValues.put("descripcion", string);
        this.myDataBase.update("des_invfisico_tmp", contentValues, string5, null);
    }
}