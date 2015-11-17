package com.boomer.omer.lastminuterager.backendprocesses;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.activities.Activity_SellTicket;
import com.boomer.omer.lastminuterager.dataobjects.Event;
import com.boomer.omer.lastminuterager.fragments.Fragment_Events;
import com.boomer.omer.lastminuterager.listhandlers.EventsListAdapter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Omer on 8/18/2015.
 */
public class DLEvents {

    public static final String EVENTS_OBJECT = "Events";
    public static final String NAME_COLUMN = "name";
    public static final String DESC_COLUMN = "description";
    public static final String DATE_COLUMN = "date";
    public static final String FOLL_COLUMN = "followers";
    public static final String TC_COLUMN = "ticketCount";
    public static final String IMAGE_COLUMN = "image";
    public static final String FOLLOWED_EVENTS_KEY = "interestedEventIDs";
    public static final String CONTACT_COLUMN = "contactInfo";
    public static final String FOLLOWER_IDS = "followerIDs";

    public static final String FOLLOWED_TOAST = "Followed event!";
    public static final String UNFOLLOWED_TOAST = "Unfollowed event!";



    static final String MESSAGE_ONGOING_DL = "Downloading Events...";




    public static void init(List<Event> list,EventsListAdapter eventsListAdapter,AsyncController asyncController){


            DownLoadData downLoadData = new DownLoadData(list,eventsListAdapter,asyncController);
            downLoadData.execute();

    }

   public static void followEvent(String id,Activity activity){
            FollowTask followTask = new FollowTask(id,activity);
            ParsePush.subscribeInBackground(id);
            followTask.execute();

   }

    public static void unFollowEvent(String id,Activity activity){
            UnFollowTask unFollowTask = new UnFollowTask(id,activity);
            ParsePush.unsubscribeInBackground(id);
            unFollowTask.execute();

    }

    public static class FollowTask extends AsyncTask<Void,Void,Void>{

        private String eventID;
        private Activity activity;

        public FollowTask(String eventID,Activity activity) {
            super();
            this.eventID = eventID;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.add(FOLLOWED_EVENTS_KEY, eventID);

            ParseQuery<ParseObject> query = ParseQuery.getQuery(EVENTS_OBJECT);
            try {
                for(ParseObject temp :query.find()){
                    if(temp.getObjectId().equals(eventID)) {
                        temp.add(FOLLOWER_IDS,currentUser.getObjectId());
                        temp.increment(FOLL_COLUMN);
                        temp.save();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                currentUser.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AppUtils.toast(activity,FOLLOWED_TOAST);
        }
    }

    public static class UnFollowTask extends AsyncTask<Void,Void,Void>{

        private String eventID;
        private Activity activity;

        public UnFollowTask(String eventID,Activity activity) {
            super();
            this.eventID = eventID;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            List<Object> followedIDs = new ArrayList<>();
            followedIDs = currentUser.getList(FOLLOWED_EVENTS_KEY);
            followedIDs.remove(eventID);
            currentUser.put(FOLLOWED_EVENTS_KEY,followedIDs);

            ParseQuery<ParseObject> query = ParseQuery.getQuery(EVENTS_OBJECT);
            try {
                for(ParseObject temp :query.find()){
                    if(temp.getObjectId().equals(eventID)) {
                        int tempInt;
                        tempInt = temp.getInt(FOLL_COLUMN);
                        temp.put(FOLL_COLUMN,tempInt-1);
                        List<Object> _followerIDs = temp.getList(FOLLOWER_IDS);
                        _followerIDs.remove(currentUser.getObjectId());
                        temp.put(FOLLOWER_IDS,_followerIDs);
                        temp.save();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                currentUser.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AppUtils.toast(activity,UNFOLLOWED_TOAST);
        }
    }


    public static class DownLoadData extends AsyncTask<Void,Event,Void>{

        public List<Event>events;
        public EventsListAdapter eventsListAdapter;
        public AsyncController asyncController;
        Dialog onGoingDownload;


        public DownLoadData(List<Event>events, EventsListAdapter eventsListAdapter,AsyncController asyncController) {

            super();
            this.events = events;
            this.eventsListAdapter = eventsListAdapter;
            this.asyncController = asyncController;

        }



        public Bitmap byteArrayToBitmap(byte[] bytes){
            Bitmap temp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return temp;
        }


        @Override
        protected Void doInBackground(Void... params) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(EVENTS_OBJECT);
            ParseUser currentUser = ParseUser.getCurrentUser();
            List<Object> followedIDs = new ArrayList<>();
            followedIDs = currentUser.getList(FOLLOWED_EVENTS_KEY);


            try {


                for(ParseObject tempParseObject: query.find()){
                    String tempName = tempParseObject.getString(NAME_COLUMN);

                    String tempDesc = tempParseObject.getString(DESC_COLUMN);
                    String tempID  = tempParseObject.getObjectId();
                    boolean isFollowed = false;
                    if(followedIDs.contains(tempID)){
                        isFollowed = true;
                        ParsePush.subscribeInBackground(tempID);////////////////////////////////////////////////////////////////////////////////
                    }


                    int tempTC = tempParseObject.getInt(TC_COLUMN);

                    ParseFile tempPF = (ParseFile)tempParseObject.get(IMAGE_COLUMN);
                    Bitmap tempBM = byteArrayToBitmap(tempPF.getData());

                    int tempFoll = tempParseObject.getInt(FOLL_COLUMN);
                    Event event = new Event(tempName,tempDesc,null,tempID,tempTC,tempBM,tempFoll,isFollowed);
                    publishProgress(event);




                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onGoingDownload = ProgressDialog.show(((Fragment_Events)asyncController).getActivity(),"",MESSAGE_ONGOING_DL,true);
            events.clear();
            eventsListAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            asyncController.downloadComplete();
            onGoingDownload.dismiss();


        }

        @Override
        protected void onProgressUpdate(Event... values) {
            super.onProgressUpdate(values);
            events.add(values[0]);
            eventsListAdapter.notifyDataSetChanged();



        }
    }

}
