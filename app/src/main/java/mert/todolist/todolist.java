package mert.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashSet;
import java.util.Set;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;


import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View;


public class todolist extends AppCompatActivity {


    ArrayList<String> shoppingList = null; //parametre atamaları
    ArrayAdapter<String> adapter = null; //kod sayesinde listview in içi objeler ile dolacak
    ListView lv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        shoppingList = getArrayVal(getApplicationContext()); //liste çeklendeğere eşittir bu değerde contexte yazılandır.
        Collections.sort(shoppingList);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingList); //Adapter eşittir yeni arrayadapter'a ve
        //geri kalan kod ile de hangi yerden çekiceğini söylüyoruz ve son olarak shoop,ngl,st'e aktarıyoruz parametre olarak tabiki.
        lv = (ListView) findViewById(R.id.ListView); // ListView id sini bulduruyoruz.
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { //eklediğimiz objelere tıklandığında
            public void onItemClick(AdapterView parent, View view, final int position, long id) { //seçile zamanı yani uzun basılırsa saçtiğinde
                String selectedItem = ((TextView) view).getText().toString(); //stringe çevir
                if (selectedItem.trim().equals(shoppingList.get(position).trim())) { //eğer seçilen düzeltme işlemi s.liste objenin konumuna eşitse
                    removeElement(selectedItem, position); //removeelementini calıstır o pozisyondaki seçili nesne için
                } else {
                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todolist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this); //yeni bir alert dialog yarat
            builder.setTitle("Ekle");
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { //pozitif butonuuza okay olayı veriyoruz
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    shoppingList.add(preferredCase(input.getText().toString()));//Okay olayı oldugunda edittezt calısacak
                    Collections.sort(shoppingList);
                    storeArrayVal(shoppingList,getApplicationContext());
                    lv.setAdapter(adapter);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //yadacancel butonu
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
    public static String preferredCase(String original)
    {
        if (original.isEmpty())
            return original;    // bu kısımdaki kod ise yazdığımız herhangi bir kelimenin baş harfini büyük ypaıyor sonunu küçük yapıyor.

        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }


    public static void storeArrayVal( ArrayList inArrayList, Context context)
    {
        Set WhatToWrite = new HashSet(inArrayList);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        prefEditor.putStringSet("myArray", WhatToWrite); //bu kod kısmında eklemiş olduğumuz objelerin hafızaya kaydediyoruz
        prefEditor.commit();
    }

    public static ArrayList getArrayVal( Context dan)
    {
        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences("dbArrayValues",Activity.MODE_PRIVATE);
        Set<String> tempSet = new HashSet<String>();
        tempSet = WordSearchGetPrefs.getStringSet("myArray", tempSet);
        return new ArrayList<String>(tempSet);    //bu kod kısmında da geri getiriyoruz
    }

    public void removeElement(String selectedItem, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shoppingList.remove(position);                              //bu kod kısmında kısaca tıklanma olayında evet denirse silecek
                Collections.sort(shoppingList);
                storeArrayVal(shoppingList, getApplicationContext());
                lv.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {     //tıklanma olayına hayır denilir ise hiç bir şey yapmıycak sayfaya geri dönecek
                dialog.cancel();
            }
        });
        builder.show();
    }

}
