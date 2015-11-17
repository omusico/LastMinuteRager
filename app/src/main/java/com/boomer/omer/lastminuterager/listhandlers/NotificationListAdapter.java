package com.boomer.omer.lastminuterager.listhandlers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.backendprocesses.DLEvents;
import com.boomer.omer.lastminuterager.dataobjects.Event;
import com.boomer.omer.lastminuterager.dataobjects.Notification;
import com.boomer.omer.lastminuterager.dataobjects.Ticket;

import java.util.List;

/**
 * Created by Omer on 8/23/2015.
 */
public class NotificationListAdapter extends ArrayAdapter<Notification> {
    static class NotificationHolder{
        TextView label;
    }

    Context context;
    int resourceID;
    List<Notification> notifications;



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationHolder notificationHolder = null;


        if(convertView==null){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            convertView = layoutInflater.inflate(resourceID,parent,false);
            notificationHolder = new NotificationHolder();
            notificationHolder.label = (TextView)convertView.findViewById(R.id.textView_label);
            convertView.setTag(notificationHolder);






        }else{
            notificationHolder=(NotificationHolder)convertView.getTag();


        }

       Notification notification = notifications.get(position);
       notificationHolder.label.setText("1 "+notification.eventName + " ticket has been posted.");
       notificationHolder.label.setTextColor(Color.GREEN);
       if(notifications.get(position).isRead){
           notificationHolder.label.setBackgroundColor(Color.BLUE);
       }else{
           notificationHolder.label.setBackgroundColor(Color.RED);
       }




        return convertView;

    }



    public NotificationListAdapter(Context context, int resource,List<Notification>list) {
        super(context, resource,list);
        this.context= context;
        this.resourceID = resource;
        this.notifications = list;

    }
}
