package com.example.dipalbhandari.nebraskacapitollive;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

import static java.time.LocalTime.parse;
import static java.util.stream.Collectors.toMap;

public class MainActivity extends AppCompatActivity  implements StreamArrayAdapter.OnItemClickListener {


    public static final String EXTRA_URL = "VideoURL";
    public static final String Video_Desc = "Desc";
    public static final String Video_Title = "VideoTitle";


    @NonNull
    private ArrayList<StreamItem> myOptions = new ArrayList<>();

    /*
     In this we have collection of list of header and Event it means for each date
     list of events are added. I have two layout of them one of header and one for event.

     */
    List<ListItem> consolidatedList = new ArrayList<>();


    private EmptyRecyclerView mRecyclerView;
    private StreamArrayAdapter adapter;
    private TextView emptyData;


    private Button banner;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myReceiver, intentFilter);


        banner = findViewById(R.id.banner);


        mRecyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);

        View emptyView = findViewById(R.id.todo_list_empty_view);
        mRecyclerView.setEmptyView(emptyView);


        //emptyData = (TextView) findViewById(R.id.empty);


        mRecyclerView.setHasFixedSize(true);




        /*
        what I am doing is I am calling the parsJSON method what it does is it create the object for my model and add them in list
         */


        parseJSON();









    }


    public void update() {



        Collections.sort(myOptions, new Comparator<StreamItem>() {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int count = 0;

            public int compare(StreamItem p1, StreamItem p2) {
                try {


                    return f.parse(p1.getDateTaken()).compareTo(f.parse(p2.getDateTaken()));


                } catch (ParseException e) {

                    throw new IllegalArgumentException(e);
                }

            }

        });

        for(StreamItem ab: myOptions){
            Log.i("value" ,ab.getDateTaken());
        }







        LinkedHashMap<String, List<StreamItem>> groupedHashMap = groupDataIntoHashMap(myOptions);

        /*
                Creating the local time
                 */
        Calendar cal = Calendar.getInstance();

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());

        Log.i("currenttime", currentTime);


        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter1.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date streamDate = null;
        String streamTime = null;
        Date dateObj1 = null;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");


        String currentTime2 = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());


        Log.i("Currenttime2", currentTime2);


        for (String date : groupedHashMap.keySet()) {


            String previousDate = date;

            HeaderItem dateItem = new HeaderItem();


            //dateItem.setDate(date);
            //consolidatedList.add(dateItem);


            Log.i("whythis", date);
            try {

                dateObj1 = dateFormatter2.parse(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date localTime = null;
            try {
                localTime = dateFormatter2.parse(currentTime2);
            } catch (ParseException e) {
                e.printStackTrace();

            }


            // dateObj1 = streamEnd date


            Log.i("DateeeeeeeFirst", String.valueOf(dateObj1));


            if (dateObj1.compareTo(localTime) > 0 || (dateObj1.compareTo(localTime) == 0)) {


                dateItem.setDate(previousDate);
                consolidatedList.add(dateItem);

                for (StreamItem pojoOfJsonArray : groupedHashMap.get(previousDate)) {


                    String streamTime2 = null;
                    Date dateObj2 = null;


                    try {
                        // Log.i("StartTime" , startTime);
                        streamDate = formatter1.parse(pojoOfJsonArray.getEndTime());

                        dateFormatter.setTimeZone(TimeZone.getDefault());
                        streamTime2 = dateFormatter.format(streamDate);


                        dateObj2 = dateFormatter.parse(streamTime2);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date localTimeFor = null;
                    try {
                        localTimeFor = dateFormatter.parse(currentTime);
                    } catch (ParseException e) {
                        e.printStackTrace();

                    }


                    if (dateObj2.compareTo(localTimeFor) > 0) {


                        EventItem generalItem = new EventItem();
                        generalItem.setStreamItem(pojoOfJsonArray);//setBookingDataTabs(bookingDataTabs);
                        consolidatedList.add(generalItem);


                    } else {


                    }

                }

                if (consolidatedList.size() == 1) {
                    consolidatedList.remove(0);
                }


            } else {

            }


        }

        adapter = new StreamArrayAdapter(this, consolidatedList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        Context context = null;

        if (adapter.getItemCount() == 0) {


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setCancelable(true);
            builder.setTitle("Alert");
            builder.setMessage("There are no streaming available right now");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }

            });
            builder.show();
        }


        DividerItemDecoration divide = new DividerItemDecoration(this, layoutManager.getOrientation());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(divide);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(MainActivity.this);


    }













    private LinkedHashMap<String, List<StreamItem>> groupDataIntoHashMap(List<StreamItem> listOfPojosOfJsonArray) {


        LinkedHashMap<String, List<StreamItem>> groupedHashMap = new LinkedHashMap<>();

        for (StreamItem pojoOfJsonArray : listOfPojosOfJsonArray) {

            String hashMapKey = pojoOfJsonArray.getDate();



            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<StreamItem> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }





        return groupedHashMap;
    }


    public void parseJSON() {


        String url = "https://netsync.unl.edu/feeds/apps/capitol_live/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject e = response.getJSONObject(i);
                        String date = e.getString("streamStart");
                        String des = e.getString("desc");
                        String newDate = e.getString("streamStart");
                        String image = e.getString("imgName");
                        String title = e.getString("category");
                        String url = e.getString("contentUrl");
                        String endDate = e.getString("streamEnd");


                        String pattern = "yyyy-MM-dd HH:mm:ss";
                        DateFormat df4 = new SimpleDateFormat("yyyy-MM-dd");


                        Date date2 = null;
                        String str4 = null;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


                        try {
                            date2 = simpleDateFormat.parse(date);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        str4 = df4.format(date2);


                        StreamItem str1 = new StreamItem(str4, title, des, image, url, newDate, endDate);




                        myOptions.add(str1);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                update();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Volley", error.toString());


            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);



        /*StreamItem str1 = new StreamItem("2019-07-12" , "Governor's News Conference" ,
                "overnor's News Conference" , "governor_icon.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-12 12:00:00", "2019-07-12 22:00:00");

        StreamItem str2 = new StreamItem("2019-007-12" , "Governor's News Conference" ,
                "FFFFFFFovernor's News Conference" , "governor_icon.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-12 10:15:00", "2019-07-12 14:15:00");


        StreamItem str3 = new StreamItem("2019-07-12" , "Governor's News Conference" ,
                "MMMMMMM's News Conference" , "governor_icon.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-12 12:15:00", "2019-07-12 15:15:00");


        StreamItem str4 = new StreamItem("2019-07-20" , "Governor's News Conference" ,
                "GGGGGGGGGovernor's News Conference" , "governor_icon.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-20 11:15:00", "2019-07-20 15:15:00");

        StreamItem str5 = new StreamItem("2019-07-11" , "Governor's News Conference" ,
                "overnor's News Conference" , "governor_icon.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-11 14:15:00", "2019-07-11 15:15:00");

        StreamItem str6 = new StreamItem("2019-07-13" , "Governor's News Conference" ,
                "overnor's News Conference" , "governor_icon1.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-13 10:15:00", "2019-07-13 14:15:00");

        StreamItem str7 = new StreamItem("2019-07-13" , "Governor's News Conference" ,
                "overnor's News Conference" , "governor_icon2.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-13 10:15:00", "2019-07-13 14:15:00");
        StreamItem str8 = new StreamItem("2019-07-14" , "Governor's News Conference" ,
                "overnor's News Conference" , "governor_icon3.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-14 10:15:00", "2019-07-14 14:15:00");
        StreamItem str9 = new StreamItem("2019-07-12" , "Governor's News Conference" ,
                "overnor's News Conference" , "governor_icon4.png" ,
                "https://d11v388ukuvwpo.cloudfront.net/livedirect/smil:lr8-gov.smil/playlist.m3u8" ,
                "2019-07-12 10:15:00", "2019-07-12 14:15:00");
        myOptions.add(str1);
        myOptions.add(str2);
        myOptions.add(str3);
        myOptions.add(str4);
        myOptions.add(str5);
        myOptions.add(str6);
        myOptions.add(str7);
        myOptions.add(str8);
        myOptions.add(str9);*/


    }





    @Override
    public void onItemClick(int position) {


       EventItem event = (EventItem) consolidatedList.get(position);


       String x = event.getStreamItem().getDateTaken();

       Log.i("Date" , x);

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter1.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = null;

        String startTime = null;
        String time = null;


        try {
            startDate = formatter1.parse(event.getStreamItem().getDateTaken());

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd, MMM, yyyy");
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("h:mm a");//this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            dateFormatter1.setTimeZone(TimeZone.getDefault());
            startTime = dateFormatter.format(startDate);
            time = dateFormatter1.format(startDate);


        } catch (ParseException e) {
            e.printStackTrace();
        }





        adapter = new StreamArrayAdapter(this, consolidatedList);


        long result = adapter.difference(x , position);







        if(result>15) {


//EventItem event = (EventItem) consolidatedList.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setCancelable(true);
            builder.setTitle("Alert");
            builder.setMessage("The streaming is not available right now.It will be available on " + startTime  + " at " + time);


            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }

            });
            builder.show();
            }


        else {



            Intent home = new Intent(this, Detail.class);

            String description = event.getStreamItem().getDesc();
            String title = event.getStreamItem().getTitle();
            String url = event.getStreamItem().getUrl();

            home.putExtra(EXTRA_URL , event.getStreamItem().getUrl());
            home.putExtra(Video_Title , title);
            home.putExtra( Video_Desc, description);
            startActivity(home);

        }


    }





    }

