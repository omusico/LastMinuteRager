package com.boomer.omer.lastminuterager.backendprocesses;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.dataobjects.Event;
import com.boomer.omer.lastminuterager.dataobjects.Ticket;
import com.boomer.omer.lastminuterager.listhandlers.TicketsListAdapter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omer on 8/20/2015.
 */
public class DLTickets {

    public static String TICKETS_OBJECT = "Tickets";
    public static String EVENT_ID_COLUMN ="eventID";
    public static String EVENT_NAME_COLUMN = "label";
    public static String SELLER_ID_COLUMN = "sellerID";
    public static String IPCount_COLUMN = "interestedPeopleCount";
    public static String DESC_COLUMN = "description";
    public static String IMAGE_COLUMN = "image";
    public static String PRICE_COLUMN = "price";
    public static String CONTACT_COLUMN = "contactInfo";

    public static String FILE_TICKET_IDS = "ticketIDs";

    static final String MESSAGE_ONGOING_DL = "Downloading Tickets...";

    public static final String DOWNLOAD_ALL = "ALL AVAILABLE TICKETS";
    public static final String DOWNLOAD_FOLLOWED = "FOLLOWED EVENTS ONLY";



    public static void init(List<Ticket> tickets,TicketsListAdapter ticketsListAdapter,Activity activity,String DownloadSelect,AsyncController asyncController){
        DownloadTickets downloadTickets = new DownloadTickets(tickets,ticketsListAdapter,activity,DownloadSelect,asyncController);
        downloadTickets.execute();

    }

    public static class DownloadTickets extends AsyncTask<Void,Ticket,Void>{

        Dialog onGoingDownload;

        List<Ticket> tickets;
        TicketsListAdapter ticketsListAdapter;
        Activity activity;
        AsyncController asyncController;

        String DownLoadSelect;




        public DownloadTickets(List<Ticket> tickets,TicketsListAdapter ticketsListAdapter,Activity activity,String DownLoadSelect,AsyncController asyncController) {
            super();
            this.tickets = tickets;
            this.ticketsListAdapter = ticketsListAdapter;
            this.activity = activity;
            this.DownLoadSelect = DownLoadSelect;
            this.asyncController =asyncController;
        }




        @Override
        protected Void doInBackground(Void... params) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(TICKETS_OBJECT);
            ParseUser currentUser = ParseUser.getCurrentUser();
            List<Object> followedEventIDs = new ArrayList<>();
            followedEventIDs = currentUser.getList(DLEvents.FOLLOWED_EVENTS_KEY);
            if(DownLoadSelect.equals(DOWNLOAD_ALL)){
                try {
                    for(ParseObject tempParseObject: query.find()){
                        publishProgress(downloadTicket(tempParseObject));


                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else if(DownLoadSelect.equals(DOWNLOAD_FOLLOWED)){
                try {
                    for(ParseObject tempParseObject: query.find()){
                        if(followedEventIDs.contains(tempParseObject.getString(EVENT_ID_COLUMN))) {
                            publishProgress(downloadTicket(tempParseObject));

                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }else{
                try {
                    for(ParseObject tempParseObject: query.find()){
                        if(DownLoadSelect.equals(tempParseObject.getString(EVENT_NAME_COLUMN))) {
                           publishProgress(downloadTicket(tempParseObject));

                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }




            return null;
        }

        public Ticket downloadTicket(ParseObject tempParseObject) throws ParseException {
            String eventID = tempParseObject.getString(EVENT_ID_COLUMN);
            String eventName = tempParseObject.getString(EVENT_NAME_COLUMN);
            String description = tempParseObject.getString(DESC_COLUMN);
            String price = tempParseObject.getString(PRICE_COLUMN);
            String sellerID = tempParseObject.getString(SELLER_ID_COLUMN);
            int followers = tempParseObject.getInt(IPCount_COLUMN);
            ParseFile tempPF = (ParseFile) tempParseObject.get(IMAGE_COLUMN);
            Bitmap tempBM = byteArrayToBitmap(tempPF.getData());
            String contactInfo = tempParseObject.getString(CONTACT_COLUMN);
            String ticketID = tempParseObject.getObjectId();

            Ticket ticket = new Ticket(eventName, description, eventID, price, sellerID, null, followers, tempBM, contactInfo,ticketID);
            return ticket;

        }

        @Override
        protected void onProgressUpdate(Ticket... values) {
            super.onProgressUpdate(values);
            tickets.add(values[0]);

            ticketsListAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tickets.clear();
            ticketsListAdapter.notifyDataSetChanged();
            onGoingDownload = ProgressDialog.show(activity, "", MESSAGE_ONGOING_DL, true);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onGoingDownload.dismiss();
            if(DownLoadSelect.equals(DOWNLOAD_ALL)){
                asyncController.downloadComplete();
            }
        }



        public Bitmap byteArrayToBitmap(byte[] bytes){
            Bitmap temp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return temp;
        }
    }

    public static class CacheTickets extends AsyncTask<Void,Void,Void>{
        List<Ticket> tickets;

        File file_ticketIDs;
        Activity activity;

        public CacheTickets(List<Ticket> tickets,Activity activity) {
            super();

            this.tickets =tickets;
            this.activity =activity;
        }

        @Override
        protected Void doInBackground(Void... params) {

            file_ticketIDs = activity.getFileStreamPath(FILE_TICKET_IDS);

            List<String> ticketIDs = new ArrayList<String>();
            for (Ticket temp : tickets) {
                ticketIDs.add(temp.ID);

            }
            file_ticketIDs.delete();
            try {
                FileOutputStream fileOutputStream = activity.getApplicationContext().openFileOutput(FILE_TICKET_IDS, activity.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
                oos.writeObject(ticketIDs);
                oos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public static void cacheInit(List<Ticket> list,Activity activity){
        CacheTickets cacheTickets = new CacheTickets(list,activity);
        cacheTickets.execute();
    }

    public static void clearCache(Activity activity){
        File file = activity.getFileStreamPath(FILE_TICKET_IDS);
        file.delete();

    }

}
