package com.boomer.omer.lastminuterager.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.activities.Activity_Main;
import com.boomer.omer.lastminuterager.activities.Activity_TicketDetails;
import com.boomer.omer.lastminuterager.backendprocesses.DLNotifications;
import com.boomer.omer.lastminuterager.backendprocesses.DLTickets;
import com.boomer.omer.lastminuterager.dataobjects.Notification;
import com.boomer.omer.lastminuterager.dataobjects.Ticket;
import com.boomer.omer.lastminuterager.listhandlers.NotificationListAdapter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_UserProfile extends android.support.v4.app.Fragment implements View.OnClickListener,ListView.OnItemLongClickListener{

    FragmentController controller;


    Button button_sellTicket;
    Button button_sync;

    ListView listView_notifications;
    List<Notification> ticketNotifications;

    NotificationListAdapter adapter;


    public Fragment_UserProfile() {

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = (FragmentController) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        syncNotifications();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AppUtils.NOTIFICATION_ID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        button_sellTicket = (Button)getActivity().findViewById(R.id.button_sell);
        button_sellTicket.setOnClickListener(this);

        button_sync = (Button)getActivity().findViewById(R.id.button_sync);
        button_sync.setOnClickListener(this);

        ticketNotifications = new ArrayList<Notification>();
        listView_notifications = (ListView)getActivity().findViewById(R.id.listView_notifications);
        adapter = new NotificationListAdapter(getActivity(),R.layout.list_notifications,ticketNotifications);
        listView_notifications.setAdapter(adapter);
        listView_notifications.setOnItemLongClickListener(this);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_profile, container, false);

    }




    @Override
    public void onClick(View v) {

        controller.pressedButton(v.getId());

    }

    public void syncNotifications(){
        ticketNotifications.clear();
        adapter.notifyDataSetChanged();
        DLNotifications.init(ticketNotifications,adapter);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent launchIntent = new Intent(getActivity(), Activity_TicketDetails.class);
        String ticketID = ((Notification)parent.getItemAtPosition(position)).ticketID;
        ((Notification)parent.getItemAtPosition(position)).isRead = false;
        adapter.notifyDataSetChanged();

        Ticket mTicket = downloadTicket(ticketID);
        launchIntent.putExtra(Fragment_Tickets.LABEL,mTicket.eventName );
        launchIntent.putExtra(Fragment_Tickets.PRICE, mTicket.price);
        launchIntent.putExtra(Fragment_Tickets.IMAGE, mTicket.image);
        launchIntent.putExtra(Fragment_Tickets.DESC, mTicket.description);
        launchIntent.putExtra(Fragment_Tickets.CONTACT, mTicket.contactInfo);
        getActivity().startActivity(launchIntent);


        String notificationID = ((Notification)parent.getItemAtPosition(position)).ID;
        notificationRead(notificationID);

        return false;
    }

    public Ticket downloadTicket(String ID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(DLTickets.TICKETS_OBJECT);
        ParseObject parseTicket = null;
        Bitmap tempBM = null;
        try {
            parseTicket  = query.get(ID);
            ParseFile tempPF = (ParseFile)parseTicket.get(DLTickets.IMAGE_COLUMN);
            tempBM = byteArrayToBitmap(tempPF.getData());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Ticket ticketObject = new Ticket(parseTicket.getString(DLTickets.EVENT_NAME_COLUMN),parseTicket.getString(DLTickets.DESC_COLUMN),parseTicket.getString(DLTickets.EVENT_ID_COLUMN),parseTicket.getString(DLTickets.PRICE_COLUMN),null,null,0,tempBM,parseTicket.getString(DLTickets.CONTACT_COLUMN),parseTicket.getObjectId());

        return ticketObject;
    }

    public Bitmap byteArrayToBitmap(byte[] bytes){
        Bitmap temp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return temp;
    }

    public void notificationRead(final String ID){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(DLNotifications.NOTIFICATION_OBJECT);
                    ParseObject temp = query.get(ID);
                    temp.put(DLNotifications.IS_READ_COL,true);
                    temp.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.start();

    }
}
