package com.boomer.omer.lastminuterager.dataobjects;

/**
 * Created by Omer on 8/15/2015.
 */
public class Notification {

    static final String TICKET_POSTED = "New tickets available...";

    public static final String NOTIFICATIONS_OBJECT = "Notifications";

    public String TYPE;
   // public String receiverID;
   // public String senderID;
    public String ticketID;
    public String receiverID;
    public String eventName;
    public boolean isRead;
    public String ID;

    public Notification(String tickedID,String receiverID,String eventName,boolean isRead, String ID ) {
        TYPE = TICKET_POSTED;
        this.ticketID=tickedID;
        this.receiverID=receiverID;
        this.eventName=eventName;
        this.isRead = isRead;
        this.ID = ID;
    }
}
