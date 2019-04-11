package com.example.sqlitelistas;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mList;
    List<Lugares> lugares;
    ArrayAdapter<Lugares> mAdapter;
    private SQLiteDatabase db;
    LugaresSQLiteHelper ludbh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicialize();


        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Lugares currentLugar = mAdapter.getItem(position);
                Toast.makeText(mAdapter.getContext(),
                        "Iniciar screen de detalle para: \n" + currentLugar.getLugar(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivityForResult(intent,2);
            }
        });

        final Button load_button = findViewById(R.id.load_button);
        load_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alternativa 1: método rawQuery()
                Cursor c = db.rawQuery("SELECT lugar, provincia FROM Lugares", null);

                lugares.removeAll(lugares);
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        String lug = c.getString(0);
                        String prov = c.getString(1);
                        Lugares lugar =new Lugares(lug, prov);
                        lugares.add(lugar);
                        mAdapter.notifyDataSetChanged();
                    } while(c.moveToNext());
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){
            assert data != null;
            String intent_accion=data.getStringExtra("accion");
            String intent_lugar= data.getStringExtra("lugar");
            String intent_provincia= data.getStringExtra("provincia");

            if(intent_accion.equals("guardar")){
                Lugares lugar = new Lugares(intent_lugar, intent_provincia);
                lugares.add(lugar);
                mAdapter.notifyDataSetChanged();

                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("lugar", intent_lugar);
                nuevoRegistro.put("provincia", intent_provincia);
                db.insert("Lugares", null, nuevoRegistro);
            }
            if(intent_accion.equals("borrar")){
                db.delete("Lugares", "lugar= '" + intent_lugar + "'", null);
                Cursor c = db.rawQuery("SELECT lugar, provincia FROM Lugares", null);

                lugares.removeAll(lugares);
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        String lug = c.getString(0);
                        String prov = c.getString(1);
                        Lugares lugar =new Lugares(lug, prov);
                        lugares.add(lugar);
                        mAdapter.notifyDataSetChanged();
                    } while(c.moveToNext());
                }
            }
        }
    }
    private void inicialize(){
        lugares = new ArrayList<Lugares>();
        mList = findViewById(R.id.lugares_list);
        mAdapter= new LugaresAdapter(this, lugares);
        mList.setAdapter(mAdapter);
        ludbh = new LugaresSQLiteHelper(this, "Lugares", null, 1);
        db = ludbh.getWritableDatabase();
    }
}

