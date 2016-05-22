package com.example.bdsemester;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания и работы с базой данных.
 */
public class DataBaseHelper extends SQLiteOpenHelper{

    /*--------------------------------------------------------------------------------------*/
    /*---------------------------------Переменные-------------------------------------------*/
    /*--------------------------------------------------------------------------------------*/
    public static final  String DATABASE_NAME = "database.db"; // Имя базы данных           //
    public static final int VERSION = 1;                       // Версия                    //

    //////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////ТАБЛИЦЫ и СТОБЦЫ/////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    // ТОВАР
    public static final String TABLE_PRODUCT = "product";          // Имя таблицы
    public static final String PRODUCT_ID = "product_id";           // ID Записи
    public static final String PRODUCT_NAME = "product_name";      // Стоблбец с названием товара

    // Продавец
    private static final String TABLE_SELLER = "seller";            // Имя таблицы
    private static final String SELLER_ID = "seller_id";            // ID продавца
    private static final String SELLER_FIO = "seller_fio";          // Фио продавца

    // Покупатель
    private static final String TABLE_BUYER = "buyer";              // Имя таблицы
    private static final String BUYER_ID = "buyer_id";              // ID Покупателя
    private static final String BUYER_FIO = "buyer_fio";            // ФИО покупателя
    private static final String BUYER_CONST_CLIENT = "const_client";// Постоянный клиент? - Логический атрибут
    private static final String DISCOUNT = "discount";              // Скидка

    // Поставщик
    private static final String TABLE_PROVIDER = "provider";       // Имя таблицы
    private static final String PROVIDER_ID = "provider_id";       // ID поставщика
    private static final String PROVIDER_FIO = "provider_fio";     // ФИО поставщика

    // Поставка. Из жругих таблиц включает ID товара и ID поставщика
    private static final String TABLE_DELIVERY = "delivery";       // Имя таблицы
    private static final String DELIVERY_ID = "delivery_id";       // ID поставки
    private static final String DELIVERY_DATE = "delivery_date";   // Дата поставки
    private static final String DELIVERY_COUNT_PRODUCT = "delivery_count_product";  // Кол-во товара в поставке

    // Корзина Из других таблиц включаети в себя: ID покупателя и ID товара
    private static final String TABLE_BASKET = "basket";           // Имя таблицы
    private static final String BASKET_ID = "basket_id";           // ID корзины
    private static final String COUNT_PRODUCT = "count_product";   // Кол-во товара

    // Учет продаж. Из других таблиц: ID товара, ID продавца
    private static final String TABLE_ACCOUNTING_SALES = "accounting_sales";  // Имя таблицы
    private static final String ACCOUNTING_SALES_ID = "accounting_sales_id";  // ID записи
    private static final String SALES_COUNT = "count_sale";                   // Число проданного товара
    private static final String SALES_SUM = "sum_sale";                       // Сумма
    private static final String SALES_DATE = "sales_date";                    // Дата


    // Информация о компании. Из других таблиц: ID поставщика, ID продавца
    private static final String TABLE_COMPANY = "company";                    // Имя таблицы
    private static final String COMPANY_ID = "company_id";                    // ID компании
    private static final String COMPANY_ADDRESS = "company_address";           // Адрес компании

    // Таблица сформированного заказа готового к отправлению
    private static final String TABLE_SEND = "send";                          // Имя таблицы
    private static final String SEND_ID = "send_id";                          // ID заказа
    private static final String SEND_ADDRESS = "send_address";                // Адрес доставки
    private static final String SEND_INDEX = "send_index";                    // Почтовый индекс
    private static final String SEND_DELIVERED = "send_delivered";            // Заказ отправлен - логичский атрибут
    private static final String SEND_GET = "send_get";                        // Заказ  получен - логический атрибут
    private static final String SEND_FREE = "send_free";                      // Бесплатная доставка  - логический атрибут


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////СКРИПТЫ ДЛЯ СОЗДАНИЯ ТАБЛИЦ//////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Таблица товаров
    private static final String CREATE_TABLE_PRODUCT = "create table "
            + TABLE_PRODUCT + " (" + PRODUCT_ID + " integer primary key autoincrement, "
            + PRODUCT_NAME + " text not null);";
    // Таблица покупателей
    private static final String CREATE_TABLE_BUYER = "create table " + TABLE_BUYER
            + " (" + BUYER_ID + " integer primary key autoincrement, "
            + BUYER_FIO + " text not null, "
            + BUYER_CONST_CLIENT + " integer, "
            + DISCOUNT + " integer);";

    // Таблица продавцов
    private static final String CREATE_TABLE_SELLER = "create table " + TABLE_SELLER
            + " (" + SELLER_ID + " integer primary key autoincrement, "
            + SELLER_FIO + " text not null);";

    // Таблица корзин
    private static final String CREATE_TABLE_BASKET = "create table " + TABLE_BASKET
            + " (" + BASKET_ID + " integer primary key autoincrement, "
            + COUNT_PRODUCT + " integer not null, "
            + BUYER_ID + " integer not null, "
            + PRODUCT_ID + " integer not null, "
            + "foreign key("+ PRODUCT_ID + ") references " + TABLE_PRODUCT + "(" + PRODUCT_ID + "), "
            + "foreign key("+ BUYER_ID + ") references " + TABLE_BUYER + "(" + BUYER_ID + "));";

    // Таблица учета продаж
    private static  final String CREATE_TABLE_ACCOUNTING_SALES = "create table " + TABLE_ACCOUNTING_SALES
            + " (" + ACCOUNTING_SALES_ID + " integer primary key autoincrement, "
            + SALES_COUNT + " integer not null, "
            + SALES_SUM + " real not null, "
            + SALES_DATE + " text not null, "
            + PRODUCT_ID + " integer not null, "
            + SELLER_ID + " integer not null, "
            + "foreign key(" + PRODUCT_ID + ") references " + TABLE_PRODUCT + "(" + PRODUCT_ID + "), "
            + "foreign key(" + SELLER_ID + ") references " + TABLE_SELLER + "(" + SELLER_ID + "));";

    // Таблица поставщиков
    private static final String CREATE_TABLE_PROVIDER = "create table " + TABLE_PROVIDER
            + "(" + PROVIDER_ID + " integer primary key autoincrement, "
            + PROVIDER_FIO + " text not null);";

    // Таблица поставок
    private static final String CREATE_TABLE_DELIVERY = "create table " + TABLE_DELIVERY
            + "(" + DELIVERY_ID + " integer primary key autoincrement, "
            + DELIVERY_DATE + "text not null, "
            + DELIVERY_COUNT_PRODUCT + " integer not null, "
            + PRODUCT_ID + " integer not null, "
            + PROVIDER_ID + " integer not null, "
            + "foreign key(" + PRODUCT_ID + ") references " + TABLE_PRODUCT + "(" + PRODUCT_ID + "), "
            + "foreign key(" + PROVIDER_ID + ") references " + TABLE_PROVIDER + "(" + PROVIDER_ID + "));";

    private static final String CREATE_TABLE_SEND = "create table " + TABLE_SEND
            + "(" + SEND_ID + " integer primary key autoincrement, "
            + SEND_ADDRESS + " text not null, "
            + SEND_INDEX + " integer not null, "
            + SEND_DELIVERED + " integer not null, "
            + SEND_GET + " integer not null, "
            + SEND_FREE + " integer not null, "
            + BASKET_ID + " integer not null, "
            + "foreign key(" + BASKET_ID +") references " + TABLE_BASKET + "(" + BASKET_ID + "));";

    private static final String CREATE_TABLE_COMPANY = "create table " + TABLE_COMPANY
            + "(" + COMPANY_ID + " integer primary key autoincrement, "
            + COMPANY_ADDRESS + " text not null);";




    /*----------------------------------Конструкторы----------------------------------------*/
    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version ){
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler){
        super(context, name, factory, version,errorHandler);
    }
    /*------------------------------------------------------------------------------------------------------------------------------------------*/

    /*-------------------------------------------------Методы класса---------------------------------------------*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCT);
        Log.w("Create table.", "Create table PRODUCT");
        db.execSQL(CREATE_TABLE_BUYER);
        Log.w("Create table.", "Create table BUYER");
        db.execSQL(CREATE_TABLE_SELLER);
        Log.w("Create table.", "Create table SELLER");
        db.execSQL(CREATE_TABLE_BASKET);
        Log.w("Create table.", "Create table BASKET");
        db.execSQL(CREATE_TABLE_ACCOUNTING_SALES);
        Log.w("Create table.", "Create table ACCOUTING SALES");
        db.execSQL(CREATE_TABLE_PROVIDER);
        Log.w("Create table.", "Create table PROVIDER");
        db.execSQL(CREATE_TABLE_DELIVERY);
        Log.w("Create table.", "Create table DELIVERY");
        db.execSQL(CREATE_TABLE_SEND);
        Log.w("Create table.", "Create table SEND");
        db.execSQL(CREATE_TABLE_COMPANY);
        Log.w("Create table.", "Create table COMPANY");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Пишем в лог, если произошла смена бд
        Log.w("SQLite", "Обновился с версии " + oldVersion + "до версии " + newVersion);

        // Дропаем старые таблицы
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_PROVIDER);
        Log.w("DROP TABLE.", "PROVIDER DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_DELIVERY);
        Log.w("DROP TABLE.", "DELIVERY DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_PRODUCT);
        Log.w("DROP TABLE.", "PRODUCT DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_SELLER);
        Log.w("DROP TABLE.", "SELLER DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_ACCOUNTING_SALES);
        Log.w("DROP TABLE.", "ACCOUNTING SALES DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_BASKET);
        Log.w("DROP TABLE.", "BASKET DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_SEND);
        Log.w("DROP TABLE.", "SEND DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_BUYER);
        Log.w("DROP TABLE.", "BUYER DROP");
        db.execSQL("DROP TABLE IF IT EXIST " + TABLE_COMPANY);
        Log.w("DROP TABLE.", "COMPANY DROP");

        // Создаем таблицы с обновленными данными
        onCreate(db);




    }

}
