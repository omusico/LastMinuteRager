package com.boomer.omer.lastminuterager.dataobjects;

import android.graphics.Bitmap;

/**
 * Created by Omer on 8/14/2015.
 */
public class Ticket {
    public String eventName;
    public String description;
    public String eventID;
    public String price;
    public String sellerID;
    public String sellerName;
    public int followers;
    public Bitmap image;
    public String contactInfo;
    public String ID;

    public Ticket(String eventName,String description,String eventID,String price,String sellerID,String sellerName,int followers,Bitmap image,String contactInfo,String ID) {
        this.eventName = eventName;
        this.description = description;
        this.eventID = eventID;
        this.price = price;
        this.sellerID = sellerID;
        this.sellerName = sellerName;
        this.followers = followers;
        this.image = image;
        this.contactInfo = contactInfo;
        this.ID = ID;
    }


}
