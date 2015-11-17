package com.boomer.omer.lastminuterager.listhandlers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.backendprocesses.DLEvents;
import com.boomer.omer.lastminuterager.dataobjects.Event;

import java.util.List;

/**
 * Created by Omer on 8/18/2015.
 */
public class EventsListAdapter extends ArrayAdapter<Event>{

    static class EventHolder{
        ImageView image;
        TextView name;
        TextView followers;
        CheckBox checkBox;
    }

    Context context;
    int resourceID;
    List<Event> events;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       EventHolder eventHolder = null;


        if(convertView==null){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            convertView = layoutInflater.inflate(resourceID,parent,false);
            eventHolder = new EventHolder();
            eventHolder.image = (ImageView)convertView.findViewById(R.id.imageView_event);
            eventHolder.name = (TextView)convertView.findViewById(R.id.textView_name);
            eventHolder.followers = (TextView)convertView.findViewById(R.id.textView_tickets);
            eventHolder.checkBox = (CheckBox)convertView.findViewById(R.id.checkBox_follow);

            convertView.setTag(eventHolder);
            eventHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox tempCheckBox = (CheckBox) v;
                    Event event = (Event) v.getTag();
                    if(((CheckBox) v).isChecked()){
                        event.isFollowed=true;
                        DLEvents.followEvent(event.ID,(Activity)getContext());
                    }else{
                        event.isFollowed = false;
                        DLEvents.unFollowEvent(event.ID,(Activity)getContext());
                    }
                }
            });





        }else{
            eventHolder=(EventHolder)convertView.getTag();


        }

        Event event = events.get(position);
        eventHolder.image.setImageBitmap(event.image);
        eventHolder.name.setText(event.name);
        eventHolder.followers.setText(event.ticketCount + " tickets available");
        eventHolder.checkBox.setChecked(event.isFollowed);
        eventHolder.checkBox.setTag(event);



        return convertView;

    }

    public EventsListAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
         this.context = context;
         this.resourceID = resource;
         this.events = events;

    }




}
