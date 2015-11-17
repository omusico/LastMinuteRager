package com.boomer.omer.lastminuterager.backendprocesses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.activities.Activity_Main;
import com.boomer.omer.lastminuterager.activities.Activity_SellTicket;
import com.boomer.omer.lastminuterager.activities.Activity_TicketDetails;
import com.boomer.omer.lastminuterager.dataobjects.Ticket;
import com.boomer.omer.lastminuterager.fragments.Fragment_Tickets;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Omer on 9/21/2015.
 */
public class NotificationReceiver extends ParsePushBroadcastReceiver  {

    static String LABEL = "label";
    static String IMAGE = "image";
    static String DESC = "description";
    static String PRICE = "price";
    static String CONTACT = "contactInfo";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        //super.onPushReceive(context, intent);
        String action = intent.getAction();
        JSONObject extras=null;
        try {
            extras = new JSONObject(intent.getStringExtra(ParsePushBroadcastReceiver.KEY_PUSH_DATA));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ParseQuery<ParseObject> query = ParseQuery.getQuery(DLTickets.TICKETS_OBJECT);
        ParseObject object ;
        try {
            AppUtils.log(extras.getString("ID"));
            object = query.get(extras.getString("ID"));
            Intent launchIntent = new Intent(context, Activity_Main.class);


            ParseQuery<ParseObject> query_search = ParseQuery.getQuery(DLNotifications.NOTIFICATION_OBJECT);
            query_search.whereEqualTo(DLNotifications.RECEIVER_ID_COL,ParseUser.getCurrentUser().getObjectId());
            query_search.whereEqualTo(DLNotifications.IS_READ_COL,false);

            int mCount = 0;
            for(ParseObject temp:query.find()){
                mCount++;
            }

            ParseObject notification = new ParseObject(DLNotifications.NOTIFICATION_OBJECT);
            notification.put(DLNotifications.RECEIVER_ID_COL, ParseUser.getCurrentUser().getObjectId());
            notification.put(DLNotifications.EVENT_NAME_COL, extras.getString(DLNotifications.EVENT_NAME_COL));
            notification.put(DLNotifications.TICKET_ID_COL,extras.getString("ID"));
            notification.put(DLNotifications.IS_READ_COL,false);
            notification.saveInBackground();



            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ticket);
            if(mCount == 1) {
                builder.setContentTitle("LMR: New Ticket!");
                builder.setContentText(extras.getString("alert"));

            }else if(mCount > 1){
                builder.setContentTitle("LMR: New Tickets!");
                builder.setContentText(Integer.toString(mCount) + " new tickets have been posted!");
            }
            builder.setAutoCancel(true);
            Intent backIntent = new Intent(context, Activity_Main.class);
            backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            stackBuilder.addParentStack(Activity_Main.class);
            stackBuilder.addNextIntent(launchIntent);


            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(AppUtils.NOTIFICATION_ID,builder.build());






        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);



    }


}
