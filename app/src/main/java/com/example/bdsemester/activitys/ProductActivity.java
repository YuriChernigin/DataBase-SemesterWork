package com.example.bdsemester.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bdsemester.DataBaseHelper;
import com.example.bdsemester.R;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {


    /*-------------------Объявление переменных и объектов----------------------*/
    private FloatingActionButton fabSaveProduct;   // Сохранить запись
    private FloatingActionButton fabRemoveProduct; // Удалить запись
    private EditText editTextProductId;            // ID Записи (EditText)
    private EditText editTextProductName;          // Название записи (EditText)
    private String productName;                    // Название записи (String)
    private int productID;                         // ID Записи (String)
    private DataBaseHelper dataBaseHelper;         // Объект для получения БД
    private SQLiteDatabase sqLiteDatabase;         // Объект для работы с БД
    private Cursor cursor;                         // Курсор для перебора элементов в БД


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*--------------------Инициализация обеъктов и переменных------------------*/
        editTextProductId = (EditText) findViewById(R.id.editTextIdProduct);
        editTextProductName = (EditText) findViewById(R.id.editTextProductName);
        fabRemoveProduct = (FloatingActionButton) findViewById(R.id.fabRemoveProduct);
        fabSaveProduct = (FloatingActionButton) findViewById(R.id.fabSaveProduct);



        productID = getIntent().getExtras().getInt("ID");

        // Начинаем работу с базой данных
        dataBaseHelper = new DataBaseHelper(getApplicationContext(), DataBaseHelper.DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        // Инициализируем курсор
        cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_PRODUCT, new String[]{DataBaseHelper.PRODUCT_ID, DataBaseHelper.PRODUCT_NAME},
                null, null, null, null, null);

        // Перемещаем курсор на первый элемент БД
        boolean stopSearch = false;
        cursor.moveToFirst();
        do {
            // Ищем нужный по ID
            if (productID == cursor.getInt(cursor.getColumnIndex(DataBaseHelper.PRODUCT_ID))) {

                productName = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PRODUCT_NAME));
                stopSearch = true;

            }

        } while (cursor.moveToNext() && !stopSearch);
        cursor.close();

        setTitle("Редактирование: \"" + productName + "\"");
        editTextProductName.setText(productName);
        editTextProductId.setText(String.valueOf(productID));

        fabRemoveProduct.setOnClickListener(this);
        fabSaveProduct.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == fabSaveProduct.getId()) {
            ContentValues content = new ContentValues();
            content.put(DataBaseHelper.PRODUCT_NAME, editTextProductName.getText().toString());

            sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            sqLiteDatabase.update(DataBaseHelper.TABLE_PRODUCT, content, DataBaseHelper.PRODUCT_ID + " = ?", new String[] { String.valueOf(productID)});

            setTitle("Редактирование: \"" + editTextProductName.getText().toString() + "\"");
            Toast toast = Toast.makeText(getApplicationContext(), "Запись сохранена в БД", Toast.LENGTH_SHORT);
            toast.show();


        } else if (v.getId() == fabRemoveProduct.getId()) {

            sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            sqLiteDatabase.delete(DataBaseHelper.TABLE_PRODUCT, DataBaseHelper.PRODUCT_ID + " = ?", new String [] { String.valueOf(productID)});
            Toast toast = Toast.makeText(getApplicationContext(), "Запись удалена", Toast.LENGTH_SHORT);
            toast.show();

            onBackPressed();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

