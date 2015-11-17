package com.boomer.omer.lastminuterager.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.activities.Activity_Main;
import com.boomer.omer.lastminuterager.activities.Activity_TicketDetails;
import com.boomer.omer.lastminuterager.backendprocesses.AsyncController;
import com.boomer.omer.lastminuterager.backendprocesses.DLEvents;
import com.boomer.omer.lastminuterager.backendprocesses.DLTickets;
import com.boomer.omer.lastminuterager.dataobjects.Ticket;
import com.boomer.omer.lastminuterager.listhandlers.TicketsListAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Tickets extends android.support.v4.app.Fragment implements Spinner.OnItemSelectedListener,AsyncController,ListView.OnItemLongClickListener {

    //FragmentController fragmentController;
    ListView ticketsListView;
    List<Ticket> tickets;
    TicketsListAdapter ticketsListAdapter;

    Spinner spinnerFilter;
    List<String> eventNames;
    ArrayAdapter<String> spinnerAdapter;

    public static String LABEL = "label";
    public static String IMAGE = "image";
    public static String DESC = "desc";
    public static String PRICE = "price";
    public static String CONTACT = "contact";


    static String FILTER = "FILTER EVENTS";


    Boolean ticketsCached;

    public Fragment_Tickets() {

    }

    public void cacheTickets() throws IOException {
        if(!ticketsCached) {
            DLTickets.cacheInit(tickets,getActivity());
            ticketsCached = true;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshList();
        //ticketsCached = false;

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
        DLTickets.init(tickets,ticketsListAdapter,getActivity(),DLTickets.DOWNLOAD_ALL,this);
        try {
            FileInputStream fileInputStream = getActivity().getApplicationContext().openFileInput(Activity_Main.FILE_EVENT_NAMES);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);

            List<String> tempEventNames = (List<String>) ois.readObject();
            if(eventNames.size()<=3){
                eventNames.addAll(tempEventNames);
                spinnerAdapter.notifyDataSetChanged();
            }

            ois.close();




        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(AppUtils.TAG, e.toString());
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            Log.d(AppUtils.TAG, e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(AppUtils.TAG, e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(AppUtils.TAG, e.toString());
        }



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        ticketsCached = false;


        ticketsListView = (ListView)getActivity().findViewById(R.id.listView_tickets);
        ticketsListView.setOnItemLongClickListener(this);
        tickets = new ArrayList<Ticket>();
        ticketsListAdapter = new TicketsListAdapter(getActivity(),R.layout.list_tickets,tickets);
        ticketsListView.setAdapter(ticketsListAdapter);

        spinnerFilter =(Spinner)getActivity().findViewById(R.id.spinner_filter);

        eventNames = new ArrayList<String>();
        eventNames.add(FILTER);
        eventNames.add(DLTickets.DOWNLOAD_ALL);
        eventNames.add(DLTickets.DOWNLOAD_FOLLOWED);



        spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,eventNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);
        spinnerFilter.setOnItemSelectedListener(this);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!((String)parent.getItemAtPosition(position)).equals(FILTER)) {
            tickets.clear();
            ticketsListAdapter.notifyDataSetChanged();
            DLTickets.init(tickets, ticketsListAdapter, getActivity(), (String) parent.getItemAtPosition(position),this);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    public void refreshList(){
        spinnerFilter.setSelection(0);
        tickets.clear();
        ticketsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void downloadComplete() {
        try {
            cacheTickets();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), Activity_TicketDetails.class);

        intent.putExtra(LABEL, ((Ticket) parent.getItemAtPosition(position)).eventName);
        intent.putExtra(PRICE, ((Ticket) parent.getItemAtPosition(position)).price);
        intent.putExtra(IMAGE, ((Ticket) parent.getItemAtPosition(position)).image);
        intent.putExtra(DESC, ((Ticket) parent.getItemAtPosition(position)).description);
        intent.putExtra(CONTACT, ((Ticket) parent.getItemAtPosition(position)).contactInfo);
        startActivity(intent);
        return false;
    }
}
