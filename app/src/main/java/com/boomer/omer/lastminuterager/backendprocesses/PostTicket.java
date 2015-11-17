package com.boomer.omer.lastminuterager.backendprocesses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.activities.Activity_SellTicket;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Omer on 8/19/2015.
 */
public class PostTicket extends AsyncTask<Void,Void,Void> {
    Intent intent;
    Activity activity;




    public PostTicket(Activity activity, Intent intent) {
        super();
        this.activity = activity;
        this.intent = intent;
    }

    @Override
    protected Void doInBackground(Void... params) {

        ParseObject parseObject = new ParseObject(Activity_SellTicket.PARSE_TICKET_KEY);
        parseObject.put(Activity_SellTicket.NAME_KEY,intent.getStringExtra(Activity_SellTicket.NAME_KEY));
        parseObject.put(Activity_SellTicket.DESC_KEY,intent.getStringExtra(Activity_SellTicket.DESC_KEY));
        parseObject.put(Activity_SellTicket.PRICE_KEY,intent.getStringExtra(Activity_SellTicket.PRICE_KEY));
        parseObject.put(Activity_SellTicket.SELLER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
        parseObject.put(Activity_SellTicket.INTERESTED_COUNT_KEY,0);
        parseObject.put(Activity_SellTicket.CONTACT_KEY,intent.getStringExtra(Activity_SellTicket.CONTACT_KEY));

        String eventID = null;
        ParseQuery<ParseObject> query = new ParseQuery(DLEvents.EVENTS_OBJECT);
        query.whereEqualTo(DLEvents.NAME_COLUMN,intent.getStringExtra(Activity_SellTicket.NAME_KEY));
        try {
            for(ParseObject temp : query.find()){
                eventID = temp.getObjectId();
                temp.increment(DLEvents.TC_COLUMN);
                temp.save();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parseObject.put(Activity_SellTicket.EVENT_ID_KEY,eventID);
        Bitmap tempBM = intent.getParcelableExtra(Activity_SellTicket.IMAGE_KEY);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        tempBM.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] bitmapdata = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParseFile tempPF = new ParseFile("image.jpg",bitmapdata);
        parseObject.put(Activity_SellTicket.IMAGE_KEY,tempPF);



        try {
            parseObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParsePush push = new ParsePush();
        push.setChannel(eventID);
        JSONObject pushData = new JSONObject();




        try {
            pushData.put("ID",parseObject.getObjectId());
            pushData.put("alert","1 " + intent.getStringExtra(Activity_SellTicket.NAME_KEY) + " ticket has just been posted!");
            pushData.put(DLNotifications.EVENT_NAME_COL,intent.getStringExtra(Activity_SellTicket.NAME_KEY));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        push.setData(pushData);
        push.sendInBackground();


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AppUtils.toast(activity,AppUtils.TICKET_POSTING);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        AppUtils.toast(activity,AppUtils.TICKET_POSTED);

    }

}
