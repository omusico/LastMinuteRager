package com.boomer.omer.lastminuterager.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.backendprocesses.AsyncController;
import com.boomer.omer.lastminuterager.backendprocesses.DLEvents;
import com.boomer.omer.lastminuterager.dataobjects.Event;
import com.boomer.omer.lastminuterager.listhandlers.EventsListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Events extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener,AsyncController {



    FragmentController controller;

    ListView eventsListView;
    List<Event> events;
    EventsListAdapter eventsListAdapter;

    public Fragment_Events() {

    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        controller = (FragmentController) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        DLEvents.init(events,eventsListAdapter,this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventsListView = (ListView)getActivity().findViewById(R.id.listView_events);
        events = new ArrayList<Event>();
        eventsListAdapter = new EventsListAdapter(getActivity(),R.layout.list_events,events);
        eventsListView.setAdapter(eventsListAdapter);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }


    @Override
    public void downloadComplete() {
        Bundle sendingBundle = new Bundle();
        ArrayList<String> eventNames = new ArrayList<String>();

        for(Event event:events){
            eventNames.add(event.name);
            //Log.d(AppUtils.TAG,event.name);
        }
        sendingBundle.putStringArrayList(AppUtils.EVENT_NAMES,eventNames);
        controller.sendData(sendingBundle);


    }
}
