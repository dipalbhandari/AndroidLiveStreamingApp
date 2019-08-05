package com.example.dipalbhandari.nebraskacapitollive;

import android.support.annotation.NonNull;

import java.util.Date;
import java.util.stream.Stream;


public class HeaderItem extends ListItem {


    @NonNull
    private String date;
    private Stream event;




    public Stream getEvent(){
        return event;
    }

    @NonNull
    public  String getDate() {

        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }


    @Override
    public int getType() {

        return TYPE_HEADER;
    }

}