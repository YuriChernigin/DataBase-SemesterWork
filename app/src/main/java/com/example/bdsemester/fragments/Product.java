package com.example.bdsemester.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bdsemester.DataBaseHelper;
import com.example.bdsemester.R;
import com.example.bdsemester.activitys.ProductActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Product.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Добавить запись
    private FloatingActionButton fabAddRecord;
    // Ресайклер
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    // Текст введенный в диалоге
    private EditText newProductName;
    // БД
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase sqLiteDataBase;

    // Временная хуйня. Уже не временная
    private List <ProductInfo> products;
    private ProductInfo product;
    private int idFromDB;
    private String nameProductFromDB;
    // Курсор для общения с бд
    Cursor cursor;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Product.
     */
    // TODO: Rename and change types and number of parameters
    public static Product newInstance(String param1, String param2) {
        Product fragment = new Product();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Product() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        getActivity().setTitle(getString(R.string.product));

        // Инициализация
        fabAddRecord = (FloatingActionButton) view.findViewById(R.id.fabAddProduct);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewProduct);
        dataBaseHelper = new DataBaseHelper(getActivity(), DataBaseHelper.DATABASE_NAME, null, 1);
        sqLiteDataBase = dataBaseHelper.getWritableDatabase();
        products = new ArrayList<ProductInfo>();
        product = new ProductInfo();
        products.clear();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // Курсор для получения данных из таблицы
        cursor = sqLiteDataBase.query(DataBaseHelper.TABLE_PRODUCT, new String[] {DataBaseHelper.PRODUCT_ID, DataBaseHelper.PRODUCT_NAME},
                null, null, null, null, null);


        // Перемещае курсор на первый элемент и если первый элемент не null, то считываем инфу с бд
        if(cursor.moveToFirst()) {


            // Начиная с первого последовательно считываем все существующие данные с таблицы
            do {
                // Добавляем первый элемент в лист с данными о товарах
                nameProductFromDB = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PRODUCT_NAME));
                idFromDB = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.PRODUCT_ID));
                product = new ProductInfo(idFromDB, nameProductFromDB);
                products.add(product);

            } while (cursor.moveToNext());

            // Передаем в наш адаптер заполненый лист и устнавливаем его в ресайклевью
            adapter = new RecyclerViewAdapter(products);
            recyclerView.setAdapter(adapter);
            cursor.close();
        }



        // Устанавливаем слушатели
        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                TextView textViewID = (TextView)view.findViewById(R.id.productId);
                String strId = textViewID.getText().toString();
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                int temp = -1;
                temp = Integer.parseInt(strId);
                intent.putExtra("ID", Integer.parseInt(strId));
                startActivity(intent);
            }

        }));
        fabAddRecord.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == fabAddRecord.getId())
        {

            LayoutInflater inflaterDialog = getActivity().getLayoutInflater();
            View layout = inflaterDialog.inflate(R.layout.dialog_add_product, null);
            newProductName = (EditText) layout.findViewById(R.id.editTextProduct);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(layout);


            builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Контент для вставки в таблицу
                    ContentValues content = new ContentValues();
                    String nameForInsertDB = newProductName.getText().toString();
                    content.put(DataBaseHelper.PRODUCT_NAME, nameForInsertDB);
                    int id = (int)sqLiteDataBase.insert(DataBaseHelper.TABLE_PRODUCT, null, content);

                    products.add(new ProductInfo(id, nameForInsertDB));
                    if(adapter == null) {
                        adapter = new RecyclerViewAdapter(products);
                        recyclerView.setAdapter(adapter);
                    }
                    else
                        adapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast toast = Toast.makeText(getActivity(), "Запись в БД не добавлена", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });



            builder.setCancelable(true);
            builder.create();
            builder.show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        getActivity().setTitle(getString(R.string.product));
        products.clear();

        sqLiteDataBase = dataBaseHelper.getReadableDatabase();
        cursor = sqLiteDataBase.query(DataBaseHelper.TABLE_PRODUCT, new String[] { DataBaseHelper.PRODUCT_ID, DataBaseHelper.PRODUCT_NAME },
                null, null, null, null, null);

        // Перемещае курсор на первый элемент и если первый элемент не null, то считываем инфу с бд
        if(cursor.moveToFirst()) {


            // Начиная с первого последовательно считываем все существующие данные с таблицы
            do {
                // Добавляем первый элемент в лист с данными о товарах
                nameProductFromDB = cursor.getString(cursor.getColumnIndex(DataBaseHelper.PRODUCT_NAME));
                idFromDB = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.PRODUCT_ID));
                product = new ProductInfo(idFromDB, nameProductFromDB);
                products.add(product);

            } while (cursor.moveToNext());

            // Передаем в наш адаптер заполненый лист и устнавливаем его в ресайклевью
            adapter = new RecyclerViewAdapter(products);
            recyclerView.setAdapter(adapter);
        }

        if (adapter != null)
            adapter.notifyDataSetChanged();




    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder>
    {
        public List <ProductInfo> products;

        RecyclerViewAdapter(List <ProductInfo> products)
        {
            this.products = products;
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_product, parent, false);
            ProductViewHolder productViewHolder = new ProductViewHolder(view);
            return productViewHolder;
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            holder.productName.setText(products.get(position).getNameProduct());
            holder.productId.setText(String.valueOf(products.get(position).getIdProduct()));
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView)
        {
            super .onAttachedToRecyclerView(recyclerView);
        }

        public class ProductViewHolder extends RecyclerView.ViewHolder
        {
            private CardView cardView;
            private TextView productName;
            private TextView productId;

            ProductViewHolder (View itemView)
            {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.cardViewProduct);
                productName = (TextView) itemView.findViewById(R.id.nameProduct);
                productId = (TextView) itemView.findViewById(R.id.productId);

            }
        }
    }

    public class ProductInfo
    {
        private int ID;
        private String nameProduct;

        ProductInfo(){
            ID = -1;
            nameProduct = null;
        };

        ProductInfo(int id, String nameProduct)
        {
            this.ID = id;
            this.nameProduct = nameProduct;
        }

        public void setData(int id, String nameProduct){
            this.ID = id;
            this.nameProduct = nameProduct;
        }

        public int getIdProduct(){
            return ID;
        }

        public String getNameProduct(){
            return nameProduct;
        }

    }

    //Класс слушателя для RecyclerView
    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private OnItemClickListener recyclerListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener){
            recyclerListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && recyclerListener != null && mGestureDetector.onTouchEvent(e)) {
                recyclerListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {  }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    }


}
