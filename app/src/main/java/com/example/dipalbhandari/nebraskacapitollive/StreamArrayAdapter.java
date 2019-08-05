
package com.example.dipalbhandari.nebraskacapitollive;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.LocalDate.now;

public class StreamArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    List<ListItem> consolidatedList = new ArrayList<>();

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.mListener = listener;
    }







    public StreamArrayAdapter(Context mContext, List<ListItem> consolidatedList) {
        this.mContext = mContext;
        this.consolidatedList = consolidatedList;
    }











    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListItem.TYPE_EVENT:
                View v1 = inflater.inflate(R.layout.list, parent, false);
                viewHolder = new EventViewHolder(v1 , mListener) ;
                break;

            case ListItem.TYPE_HEADER:
                View v2 = inflater.inflate(R.layout.header, parent, false);
                viewHolder = new HeaderViewHolder(v2);
                break;
        }

        return viewHolder;
    }







    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {



        switch (viewHolder.getItemViewType()) {
            case ListItem.TYPE_HEADER:

                HeaderItem dateItem = (HeaderItem) consolidatedList.get(i);



                HeaderViewHolder dateViewHolder = (HeaderViewHolder) viewHolder;
               // ((HeaderViewHolder) viewHolder).container1.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_animation));



                //SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd yyyy");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String compareDate = formatter.format(date);










                    if (compareDate.equalsIgnoreCase(dateItem.getDate())) {

                        dateViewHolder.mDate.setText("Today");
                    }

                    else {

                        String finalDate = null;
                        DateFormat to   = new SimpleDateFormat("E, MMM dd yyyy"); // wanted format
                        DateFormat from = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                             finalDate = to.format(from.parse(dateItem.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        dateViewHolder.mDate.setText(finalDate);

                    }








                break;






            case ListItem.TYPE_EVENT:




                EventItem event = (EventItem) consolidatedList.get(i);

                EventViewHolder holder = (EventViewHolder) viewHolder;

               // ((EventViewHolder) viewHolder).container.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_animation));




                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formatter1.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date startDate = null;
                Date endDate = null;
                String startTime = null;
                String endTime = null;

                try {
                    startDate = formatter1.parse(event.getStreamItem().getDateTaken());
                    endDate = formatter1.parse(event.getStreamItem().getEndTime());
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(" h:mm a"); //this format changeable
                    dateFormatter.setTimeZone(TimeZone.getDefault());
                    startTime = dateFormatter.format(startDate);
                    endTime = dateFormatter.format(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

               // String result = startTime + " - " + endTime;
                String result = startTime;

                difference(event.getStreamItem().getDateTaken(), i);



                holder.mDate.setText(result);

                //holder.mDate.setText(event.getStreamItem().getEndTime());
                holder.mDesc.setText(event.getStreamItem().getDesc());
                holder.mTitle.setText(event.getStreamItem().getTitle());





                String image = event.getStreamItem().getImage();

                if (image.contains("governor_icon1.png")) {

                    holder.mImageView.setImageResource(R.drawable.boardofeducation);
                } else if (image.contains("governor_icon2.png")) {
                    holder.mImageView.setImageResource(R.drawable.legislature);

                }

                else if (image.contains("governor_icon3.png")) {
                    holder.mImageView.setImageResource(R.drawable.governor);

                }

                else if (image.contains("governor_icon4.png")) {
                    holder.mImageView.setImageResource(R.drawable.courtofappeals);

                }



                break;


        }
    }






    private static  class EventViewHolder extends RecyclerView.ViewHolder  {


        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDate;
        public TextView mDesc;
        public VideoView videoView;


        LinearLayout container;






        public EventViewHolder(@NonNull View itemView , final OnItemClickListener listener) {
            super(itemView);




            container = itemView.findViewById(R.id.container);
            mImageView = itemView.findViewById(R.id.icon);
            mTitle = itemView.findViewById(R.id.destime);
            mDate = itemView.findViewById(R.id.desIntent);
            mDesc = itemView.findViewById(R.id.desDetail);
            //videoView = itemView.findViewById(R.id.videoView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener !=null){

                        int position = getAdapterPosition();

                        if(position!=RecyclerView.NO_POSITION){


                            listener.onItemClick(position);
                        }

                    }

                }
            });



        }



    }


    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        protected TextView mDate;
        LinearLayout container1;

        public HeaderViewHolder(@NonNull View itemView) {



            super(itemView);
            container1 = itemView.findViewById(R.id.container1);
            mDate = itemView.findViewById(R.id.date);
        }
    }



    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }


    /*
    This method is clacuting the difference
    between the current
     */
    public long difference(String data , int position){


          /*
                Creating the local time
                 */
        Calendar cal = Calendar.getInstance();

        String currentTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());

        //Log.i("Current Time" , currentTime);

        EventItem event = (EventItem) consolidatedList.get(position);


        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date streamDate = null;
        String streamTime = null;
        Date dateObj1 = null;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter1.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            // Log.i("StartTime" , startTime);
            streamDate = formatter1.parse(event.getStreamItem().getDateTaken());

            dateFormatter.setTimeZone(TimeZone.getDefault());
            streamTime = dateFormatter.format(streamDate);
            dateObj1 = dateFormatter.parse(streamTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date localTime = null;
        try {
            localTime = dateFormatter.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();

        }





        long result = dateObj1.getTime() - localTime.getTime();

         //Log.i("Difference Time" , String.valueOf(result));
        long minute = result/60000;


        return minute;
    }




}
















