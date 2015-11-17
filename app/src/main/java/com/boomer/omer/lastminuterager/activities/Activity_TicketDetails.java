package com.boomer.omer.lastminuterager.activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.fragments.Fragment_Tickets;

import org.w3c.dom.Text;


public class Activity_TicketDetails extends ActionBarActivity implements View.OnClickListener {


    ImageView imageView_thumbnail;
    TextView textView_price;
    EditText editText_label;
    EditText editText_desc;
    EditText editText_contact;
    Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);
        imageView_thumbnail = (ImageView)findViewById(R.id.imageView_ticketimg);
        textView_price = (TextView)findViewById(R.id.textView_price);
        editText_label = (EditText)findViewById(R.id.editText_label);
        editText_desc = (EditText)findViewById(R.id.editTextd_desc);
        editText_contact = (EditText)findViewById(R.id.editText_contact);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(this);


        imageView_thumbnail.setImageBitmap((android.graphics.Bitmap) getIntent().getParcelableExtra(Fragment_Tickets.IMAGE));
        textView_price.setText(getIntent().getStringExtra(Fragment_Tickets.PRICE));
        editText_label.setText(getIntent().getStringExtra(Fragment_Tickets.LABEL));
        editText_desc.setText(getIntent().getStringExtra(Fragment_Tickets.DESC));
        editText_contact.setText(getIntent().getStringExtra(Fragment_Tickets.CONTACT));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_ticket_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_back){finish();}

    }
}
