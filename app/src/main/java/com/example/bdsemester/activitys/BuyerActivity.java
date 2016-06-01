package com.example.bdsemester.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bdsemester.DataBaseHelper;
import com.example.bdsemester.R;

public class BuyerActivity extends AppCompatActivity implements View.OnClickListener{


    /*----------------------Блок объявления-----------------------*/
    // Переменная для определения создается запись или редактируется
    private boolean isCreate;
    private int idFromIntent;
    // ФИО покупателя
    private String buyerName;
    private EditText edTxtBuyerName;
    // ID
    private EditText edTxtBuyerId;
    // Конопки добавления, удаления и сохранения записи
    private FloatingActionButton fabAddBuyer; // Добавить
    private FloatingActionButton fabRemoveBuyer; // Удалить
    private FloatingActionButton fabSaveBuyer; // Сохранить


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Получем значение параметра переданного интентом
        Intent intent = getIntent();
        isCreate = intent.getBooleanExtra("CREATE", false);
        idFromIntent = intent.getExtras().getInt("ID");

        /*--------------------------Блок инициализации-------------------------*/
        edTxtBuyerName = (EditText) findViewById(R.id.edTextBuyerFIO);
        edTxtBuyerId = (EditText) findViewById(R.id.edTxtBuyerId);
        fabAddBuyer = (FloatingActionButton) findViewById(R.id.fabAddBuyerActiv);
        fabRemoveBuyer = (FloatingActionButton) findViewById(R.id.fabRemoveBuyer);
        fabSaveBuyer = (FloatingActionButton) findViewById(R.id.fabSaveBuyer);
        /*---------------------------------------------------------------------*/

        // Устанавливаем видмость кнопок
        if (isCreate) {
            fabAddBuyer.setVisibility(View.VISIBLE);
            fabRemoveBuyer.setVisibility(View.INVISIBLE);
            fabSaveBuyer.setVisibility(View.INVISIBLE);
        } else {
            fabAddBuyer.setVisibility(View.INVISIBLE);
            fabRemoveBuyer.setVisibility(View.VISIBLE);
            fabSaveBuyer.setVisibility(View.VISIBLE);
            edTxtBuyerName.setText(getRecFromDB(idFromIntent));
            edTxtBuyerId.setText(String.valueOf(idFromIntent));
        }

        // Слушатели
        fabAddBuyer.setOnClickListener(this);
        fabRemoveBuyer.setOnClickListener(this);
        fabSaveBuyer.setOnClickListener(this);

    }

    public String getRecFromDB(int id){

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext(), DataBaseHelper.DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_BUYER, new String[] { DataBaseHelper.BUYER_ID, DataBaseHelper.BUYER_FIO}, null, null, null, null, null);

        if (cursor.moveToFirst()){
            boolean isFind = false;
            do {
                if (cursor.getInt(cursor.getColumnIndex(DataBaseHelper.BUYER_ID)) == id){
                    return cursor.getString(cursor.getColumnIndex(DataBaseHelper.BUYER_FIO));
                }
            } while (cursor.moveToNext() && !isFind);
        }


        return null;
    }

    // Метод для создания записи
    public int changeBuyerDB(String name, int id, String MODIFICATION){

        int createId = -1;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext(), DataBaseHelper.DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(DataBaseHelper.BUYER_FIO, name);

        if (MODIFICATION == "CREATE"){
            createId = (int)sqLiteDatabase.insert(DataBaseHelper.TABLE_BUYER, null, content);
        } else if (MODIFICATION == "DELETE" && id != -1){
            sqLiteDatabase.delete(DataBaseHelper.TABLE_BUYER, DataBaseHelper.BUYER_ID + " = ?", new String[]{ String.valueOf(id) });
        } else if (MODIFICATION == "UPDATE" && id != -1){
            sqLiteDatabase.update(DataBaseHelper.TABLE_BUYER, content, DataBaseHelper.BUYER_ID + " = ?", new String[]{ String.valueOf(id) });
        }

        // Возвращет ID созданной записи
        return createId;
    }

    public int changeBuyerDB(String name, String MODIFICATION){
        return changeBuyerDB(name, -1, MODIFICATION);
    }




    @Override
    public void onClick(View v) {

        // Нажатие на кнопку добавить запись
        if ( v.getId() == fabAddBuyer.getId()){
            if (edTxtBuyerName.length() != 0){
                int id = changeBuyerDB(edTxtBuyerName.getText().toString(), "CREATE");
                edTxtBuyerId.setText(String.valueOf(id));
                Toast.makeText(getApplicationContext(), "Запись добавлена в БД", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Введите имя", Toast.LENGTH_SHORT);
        }
        // Нажатие на кнопку удалить
        else if ( v.getId() == fabRemoveBuyer.getId()){
            changeBuyerDB(edTxtBuyerName.getText().toString(), Integer.parseInt(edTxtBuyerId.getText().toString()), "DELETE");
            Toast.makeText(getApplicationContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        // Нажатие на кнопку сохранить
        else if ( v.getId() == fabSaveBuyer.getId()){
            changeBuyerDB(edTxtBuyerName.getText().toString(), Integer.parseInt(edTxtBuyerId.getText().toString()), "UPDATE");
            Toast.makeText(getApplicationContext(), "Запись обновлена", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }
}
