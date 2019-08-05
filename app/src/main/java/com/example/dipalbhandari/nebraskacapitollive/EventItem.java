package com.example.dipalbhandari.nebraskacapitollive;

import android.support.annotation.NonNull;

import java.util.List;

import static com.example.dipalbhandari.nebraskacapitollive.ListItem.TYPE_EVENT;

public class EventItem extends ListItem {

    @NonNull
    private StreamItem event;





    @NonNull
    public StreamItem getStreamItem() {
        return event;
    }

    // here getters and setters
    // for title and so on, built
    // using event


    @NonNull
    public void setStreamItem(StreamItem event) {

        this.event = event;
    }

    @Override
    public int getType() {

        return TYPE_EVENT;
    }

}
