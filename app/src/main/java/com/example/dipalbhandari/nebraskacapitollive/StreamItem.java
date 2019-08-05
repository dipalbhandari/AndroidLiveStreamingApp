package com.example.dipalbhandari.nebraskacapitollive;

import java.util.Date;

public class StreamItem {

    public String date;
    public String title;
    public String desc;
    public String image;
    public String url;
    public String dateTaken;
    public String endTime;



 public StreamItem(String date , String title , String desc , String image , String url , String dateTaken , String endTime) {
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.url = url;
        this.dateTaken = dateTaken;
        this.endTime = endTime;
    }




  public String getUrl(){
      return url;

    }

    public void setUrl(String url){

      this.url = url;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }



    public String getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = date;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}