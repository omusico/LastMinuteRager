package com.boomer.omer.lastminuterager.listhandlers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.dataobjects.Event;
import com.boomer.omer.lastminuterager.dataobjects.Ticket;

import java.util.List;

/**
 * Created by Omer on 8/18/2015.
 */
public class TicketsListAdapter extends ArrayAdapter<Ticket> {
    static class TicketHolder{
        ImageView image;
        TextView eventName;
        TextView description;
        TextView price;
        //TextView followers;
    }

    Context context;
    int resourceID;
   List<Ticket> tickets;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TicketHolder ticketHolder = null;

        if(convertView==null){
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            convertView = layoutInflater.inflate(resourceID,parent,false);
            ticketHolder = new TicketHolder();
            ticketHolder.image = (ImageView)convertView.findViewById(R.id.imageView_ticket);
            ticketHolder.eventName = (TextView)convertView.findViewById(R.id.textView_eventName);
            ticketHolder.description = (TextView)convertView.findViewById(R.id.textView_description);
            //ticketHolder.followers = (TextView)convertView.findViewById(R.id.textView_followers);
            ticketHolder.price = (TextView)convertView.findViewById(R.id.textView_price);
            convertView.setTag(ticketHolder);
        }else{
            ticketHolder=(TicketHolder)convertView.getTag();
        }
        Ticket ticket = tickets.get(position);
        ticketHolder.image.setImageBitmap(ticket.image);
        ticketHolder.eventName.setText(ticket.eventName);
        ticketHolder.description.setText(ticket.description);
        //ticketHolder.followers.setText(ticket.followers + " people interested");
        ticketHolder.price.setText(ticket.price);






        return convertView;
    }

    public TicketsListAdapter(Context context, int resource, List<Ticket> tickets) {
        super(context, resource, tickets);
        this.context = context;
        this.resourceID = resource;
        this.tickets = tickets;


    }

}
