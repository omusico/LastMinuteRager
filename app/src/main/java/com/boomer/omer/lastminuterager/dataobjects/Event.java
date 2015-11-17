package com.boomer.omer.lastminuterager.dataobjects;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omer on 8/15/2015.
 */
public class Event {
    public String name;
    public String description;
    public String dates;
    public String ID;
    public int ticketCount;
    public Bitmap image;
    public int followers;
    public boolean isFollowed;

    public Event(String name, String description,String date,String ID, int ticketCount, Bitmap image,int followers,boolean isFollowed) {
        this.name = name;
        this.description = description;
        this.dates = date;
        this.ID= ID;
        this.ticketCount = ticketCount;
        this.image = image;
        this.followers = followers;
        this.isFollowed = isFollowed;


    }

}
