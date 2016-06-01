package com.example.bdsemester.my_classes;

/**
 * Created by www_c on 01.06.2016.
 */
public class BuyerInfo {

    /*-----------------Переменные и объекты----------------*/
    private int id;                        // ID записи
    private String name;                   // Имя покупателя


    // Конструктор по умолчанию
    public BuyerInfo(){
        id = -1;
        name = null;
    }

    // Конструктор с параметрами
    public BuyerInfo(int id, String name){
        this.id = id;
        this.name = name;
    }

    // Установить id и имя
    public void setData(int id, String name){
        this.id = id;
        this.name = name;
    }

    // Установить id
    public void setData(int id){
        this.id = id;
    }

    // Установить имя
    public void setData(String name){
        this.name = name;
    }

    // Получить id
    public int getIdBuyer(){
        return this.id;
    }

    // Получить имя
    public String getNameBuyer(){
        return this.name;
    }

}
