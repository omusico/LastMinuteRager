package com.boomer.omer.lastminuterager.backendprocesses;

import android.os.AsyncTask;

import com.boomer.omer.lastminuterager.dataobjects.Notification;
import com.boomer.omer.lastminuterager.listhandlers.NotificationListAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Omer on 9/22/2015.
 */
public class DLNotifications  {
    public static String NOTIFICATION_OBJECT = "Notifications";
    public static String RECEIVER_ID_COL = "receiverID";
    public static String TICKET_ID_COL = "ticketID";
    public static String EVENT_NAME_COL = "eventName";
    public static String IS_READ_COL = "isRead";



    public static void init(List<Notification> notificationList,NotificationListAdapter listAdapter){
        DownloadNotifications downLoadData = new DownloadNotifications(notificationList,listAdapter);
        downLoadData.execute();
    }

    public static class DownloadNotifications extends AsyncTask<Void,Notification,Void>{

        private NotificationListAdapter mListAdapter;
        private List<Notification> mList;

        public DownloadNotifications(List<Notification> notificationList,NotificationListAdapter listAdapter) {
            super();
            mListAdapter = listAdapter;
            mList = notificationList;
        }

        @Override
        protected void onProgressUpdate(Notification... values) {
            super.onProgressUpdate(values);

           mList.add(values[0]);
           mListAdapter.notifyDataSetChanged();


        }


        @Override
        protected Void doInBackground(Void... params) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery(NOTIFICATION_OBJECT);
            query.whereEqualTo(RECEIVER_ID_COL, ParseUser.getCurrentUser().getObjectId());
            try {
                for(ParseObject temp : query.find()){
                    Notification downloadedNotification =  new Notification(temp.getString(TICKET_ID_COL),temp.getString(RECEIVER_ID_COL),temp.getString(EVENT_NAME_COL),temp.getBoolean(IS_READ_COL),temp.getObjectId());
                    publishProgress(downloadedNotification);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }







}
