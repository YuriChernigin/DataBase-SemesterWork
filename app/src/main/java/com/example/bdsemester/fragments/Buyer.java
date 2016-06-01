package com.example.bdsemester.fragments;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;

import com.example.bdsemester.DataBaseHelper;
import com.example.bdsemester.R;
import com.example.bdsemester.activitys.BuyerActivity;
import com.example.bdsemester.my_classes.BuyerInfo;

import java.util.ArrayList;
import java.util.List;

/*
   Фрагмент со списком Покупателей
 */
public class Buyer extends Fragment implements View.OnClickListener{

    /*--------------------Объявление переменных и объектов----------------*/
    // Кнопка добавить, осущ. переход в активити для создания записи
    private FloatingActionButton fabAddBuyer;
    // Ресайклер вью
    private RecyclerView recyclerView;
    // Адаптер для ресайклера
    private RecyclerViewAdapter rvAdapter;
    // Лист с объектами класса BuyerInfo
    private List <BuyerInfo> listBuyers;
    // Id
    private int ID;
    // ФИО
    private String FIO;

    private OnFragmentInteractionListener mListener;


    public Buyer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer, container, false);
        getActivity().setTitle(getString(R.string.buyer));

        /*-------------------------------Инициализация-----------------------------*/
        fabAddBuyer = (FloatingActionButton) view.findViewById(R.id.fabAddBuyer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewBuyer);
        listBuyers = new ArrayList<BuyerInfo>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        listBuyers.clear();
        listBuyers = getDataDB();
        rvAdapter = new RecyclerViewAdapter(listBuyers);
        recyclerView.setAdapter(rvAdapter);

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getActivity().getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                TextView txtViewID = (TextView) view.findViewById(R.id.textViewBuyerId);
                int idItemSelect = Integer.parseInt(txtViewID.getText().toString());

                Intent intent = new Intent(getActivity(), BuyerActivity.class);
                intent.putExtra("CREATE", false);
                intent.putExtra("ID", idItemSelect);
                startActivity(intent);
            }
        }));

        // Listeners
        fabAddBuyer.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
//
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

        if (v.getId() == fabAddBuyer.getId()){
            Intent intent = new Intent(getActivity(), BuyerActivity.class);
            intent.putExtra("CREATE", true);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        listBuyers.clear();
        listBuyers = getDataDB();

        rvAdapter = new RecyclerViewAdapter(listBuyers);
        recyclerView.setAdapter(rvAdapter);
        rvAdapter.notifyDataSetChanged();
    }

    // Методы для получения записей из бд
    public List <BuyerInfo> getDataDB(){

        List <BuyerInfo> tempListBuyers = new ArrayList<>();

        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext(),
                DataBaseHelper.DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DataBaseHelper.TABLE_BUYER, new String[] { DataBaseHelper.BUYER_ID, DataBaseHelper.BUYER_FIO},
                null, null, null, null, null);

        if ( cursor.moveToFirst() ){
            do {
                ID = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.BUYER_ID));
                FIO = cursor.getString(cursor.getColumnIndex(DataBaseHelper.BUYER_FIO));
                tempListBuyers.add(new BuyerInfo(ID, FIO));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return tempListBuyers;
    }


    /*-------------------Переопределяем адаптер для ресайклер-вью-----------------*/
    private class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.BuyerViewHolder>{

        /*---------------------Объявление переменных----------------------*/
        private List <BuyerInfo> listBuyers;        // Лист с покупателями

        // Конструктор с параметрами
        public RecyclerViewAdapter(List <BuyerInfo> listBuyers){
            this.listBuyers = listBuyers;
        }

        @Override
        public int getItemCount(){
            return listBuyers.size();
        }

        @Override
        public BuyerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_view_buyer, viewGroup, false);

            BuyerViewHolder buyerViewHolder =  new BuyerViewHolder(view);
            return buyerViewHolder;
        }

        @Override
        public void onBindViewHolder(BuyerViewHolder buyerViewHolder, int i){
            buyerViewHolder.buyerName.setText(listBuyers.get(i).getNameBuyer());
            buyerViewHolder.buyerId.setText(String.valueOf(listBuyers.get(i).getIdBuyer()));
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView){
            super .onAttachedToRecyclerView(recyclerView);
        }



        public class BuyerViewHolder extends RecyclerView.ViewHolder{
            private CardView cardViewBuyer;
            private TextView buyerId;
            private TextView buyerName;

            BuyerViewHolder(View cardView){
                super (cardView);
                cardViewBuyer = (CardView) cardView.findViewById(R.id.cardViewBuyer);
                buyerId = (TextView) cardView.findViewById(R.id.textViewBuyerId);
                buyerName = (TextView) cardView.findViewById(R.id.textViewBuyerName);
            }
        }
    }


    /*------------------Слушатель для ресайклер вью-------------------------*/
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

}
