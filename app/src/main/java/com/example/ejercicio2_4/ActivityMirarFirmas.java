package com.example.ejercicio2_4;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejercicio2_4.configuraciones.Transacciones;
import com.example.ejercicio2_4.configuraciones.SQLiteConexion;

import java.util.ArrayList;

public class ActivityMirarFirmas extends AppCompatActivity {
    SQLiteConexion conexion;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Firmas> firmasList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirar_firmas);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        recyclerView = (RecyclerView) findViewById(R.id.vista_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        firmasList = new ArrayList<>();
        obtnerFirmas();
        adapter = new Adapter(firmasList);
        recyclerView.setAdapter(adapter);
    }

    private void obtnerFirmas(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        Firmas firmas = null;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.tablasignatures,null);
        while (cursor.moveToNext()){
            firmas = new Firmas();
            firmas.setId(cursor.getInt(0));
            firmas.setDescripcion(cursor.getString(1));
            firmas.setImagen(cursor.getString(2));
            firmasList.add(firmas);
        }
    }
}