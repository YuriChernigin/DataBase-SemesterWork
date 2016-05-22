package com.example.bdsemester;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bdsemester.fragments.AccountingSales;
import com.example.bdsemester.fragments.Basket;
import com.example.bdsemester.fragments.Buyer;
import com.example.bdsemester.fragments.Company;
import com.example.bdsemester.fragments.Delivery;
import com.example.bdsemester.fragments.Product;
import com.example.bdsemester.fragments.ProviderMan;
import com.example.bdsemester.fragments.Seller;
import com.example.bdsemester.fragments.SendOrder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    //Фрагменты
    private AccountingSales fragmentAccounting;  // Учет продаж
    private Basket fragmentBasket;               // Корзина
    private Buyer fragmentBuyer;                 // Покупатель
    private Company fragmentCompany;             // Компания
    private Delivery fragmentDelivery;           // Поставка
    private Product fragmentProduct;             // Товар
    private ProviderMan fragmentProvider;        // Поставщик
    private Seller fragmentSeller;               // Покупатель
    private SendOrder fragmentSend;              // Отправка заказа

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Инициализация фрагментов
        fragmentAccounting = new AccountingSales();
        fragmentBasket = new Basket();
        fragmentBuyer = new Buyer();
        fragmentCompany = new Company();
        fragmentDelivery = new Delivery();
        fragmentProduct = new Product();
        fragmentProvider = new ProviderMan();
        fragmentSeller = new Seller();
        fragmentSend = new SendOrder();


        setTitle(R.string.main_title);
        drawer.openDrawer(GravityCompat.START);




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_product) {
            replaceFragment(fragmentProduct);
        } else if (id == R.id.nav_basket) {
            replaceFragment(fragmentBasket);
        } else if (id == R.id.nav_accounting_sale) {
            replaceFragment(fragmentAccounting);
        } else if (id == R.id.nav_seller) {
            replaceFragment(fragmentSeller);
        } else if (id == R.id.nav_delivery) {
            replaceFragment(fragmentDelivery);
        } else if (id == R.id.nav_provider) {
            replaceFragment(fragmentProvider);
        } else if (id == R.id.nav_buyer){
            replaceFragment(fragmentBuyer);
        } else if (id == R.id.nav_company){
            replaceFragment(fragmentCompany);
        } else if (id == R.id.nav_send_order){
            replaceFragment(fragmentSend);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentCotainer, fragment).addToBackStack(null);
        transaction.commit();

    }
}
