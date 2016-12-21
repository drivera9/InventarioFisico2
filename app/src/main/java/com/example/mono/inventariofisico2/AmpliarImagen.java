package com.example.mono.inventariofisico2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

public class AmpliarImagen extends Activity {


    String nombre = "";
    String strPath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ampliar_imagen);

        Bundle bundle = this.getIntent().getExtras();
        nombre = bundle.getString("nombre");
        strPath = bundle.getString("path");
        ImageView myImage = (ImageView) findViewById(R.id.imageView3);


        Matrix matrix = new Matrix();
        matrix.postRotate(90.0f);

        System.out.println(strPath);
        //String strPath = "/storage/E6E7-2A20/METRO/" + nombre;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        Bitmap bm = BitmapFactory.decodeFile(strPath, options);
        //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

        myImage.setImageBitmap(bm);
        /*try {
            if (imgFile.exists()) {
                ImageView myImage = (ImageView) findViewById(R.id.imageView);
                myImage.setImageURI(Uri.fromFile(imgFile));

            }
        }catch (Exception e){
        }*/

    }
}
