package com.example.ejercicio2_4;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ejercicio2_4.configuraciones.SQLiteConexion;
import com.example.ejercicio2_4.configuraciones.Transacciones;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity  {

    panelFirmas panelFirmas;
    Button NuevaFirma, VerFirmas;
    Button Salvar;
    EditText Descripcion;
    SQLiteConexion conexion;
    Bitmap ima;
    boolean estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        estado=true;
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        NuevaFirma = (Button) findViewById(R.id.btnNewFirma);
        Salvar = (Button) findViewById(R.id.btnGuardar);
        VerFirmas = (Button)findViewById(R.id.btnGuardadas);
        Descripcion = (EditText) findViewById(R.id.textDescripcion);
        panelFirmas = (panelFirmas) findViewById(R.id.panelFirma);


        NuevaFirma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("DESEAS UNA NUEVA FIRMA?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                panelFirmas.nuevoDibujo();
                                Descripcion.setText("");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                            }
                        }).show();

            }
        });

        Salvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                firmas();
            }
        });

        VerFirmas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMirarFirmas.class);
                startActivity(intent);
            }
        });

    }

    private void firmas(){
        if(panelFirmas.borrado){
            Toast.makeText(getApplicationContext(), "ESTA VACIO!"
                    ,Toast.LENGTH_LONG).show();
            return;
        }else if(Descripcion.getText().toString().trim().isEmpty()){
            Toast.makeText(getApplicationContext(), "NECESITAS UNA DESCRIPCION!"
                    ,Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Transacciones.descripcion,Descripcion.getText().toString());
        ByteArrayOutputStream bay = new ByteArrayOutputStream(10480);
        Bitmap bitmap = Bitmap.createBitmap(panelFirmas.getWidth(), panelFirmas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        panelFirmas.draw(canvas);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , bay);
        byte[] bl = bay.toByteArray();
        String img= Base64.encodeToString(bl,Base64.DEFAULT);
        values.put(Transacciones.imagen, img);
        Long result = db.insert(Transacciones.tablasignatures, Transacciones.id, values);
        Toast.makeText(getApplicationContext(), "Firma Lista!"
                ,Toast.LENGTH_LONG).show();
        panelFirmas.nuevoDibujo();
        Descripcion.setText("");
        db.close();
    }
}