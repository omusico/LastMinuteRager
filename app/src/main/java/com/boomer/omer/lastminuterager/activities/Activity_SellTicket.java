package com.boomer.omer.lastminuterager.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.fragments.Fragment_Events;
import com.parse.ParseObject;

import java.io.IOException;
import java.util.ArrayList;


public class Activity_SellTicket extends ActionBarActivity implements View.OnClickListener,Spinner.OnItemSelectedListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String DATA_KEY = "data";

    static final String IMAGE_NULL = "You must take a picture of the ticket...";
    static final String NAME_NULL = "You must chose an even name from the list...";
    static final String DESC_NULL = "You must type a description for your ticket/deal...";
    static final String PRICE_NULL = "You must type a price...";
    static final String CONTACT_NULL = "You must type contact info...";
    static final String POSTING = "Posting your ticket...";

    public static final String PARSE_TICKET_KEY = "Tickets";
    public static final String EVENT_ID_KEY = "eventID";
    public static final String NAME_KEY = "label";
    public static final String SELLER_ID_KEY = "sellerID";
    public static final String INTERESTED_COUNT_KEY = "interestedPeopleCount";
    public static final String PRICE_KEY = "price";
    public static final String DESC_KEY = "description";
    public static final String IMAGE_KEY = "image";
    public static final String CONTACT_KEY = "contactInfo";

    public static final String DEFAULT_SPINNER_TEXT = "Pick an event";


    ImageView imageView_ticketPic;
    Spinner spinner_eventName;
    EditText editText_description;
    EditText editText_price;
    EditText editTexT_contact;

    Button button_cancel;
    Button button_reset;
    Button button_post;

    Bundle arguments;
    ArrayList<String> eventNames;

    String chosenEventName;
    String description;
    String price;
    String contact;
    Boolean imageNull;
    Bitmap imageData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_ticket);

        imageView_ticketPic = (ImageView) findViewById(R.id.imageView_ticketPic);
        spinner_eventName = (Spinner)findViewById(R.id.spinner_eventName);
        editText_description = (EditText)findViewById(R.id.editText_decription);
        editText_price = (EditText) findViewById(R.id.editText_price);
        editTexT_contact = (EditText)findViewById(R.id.editText_contact);

        button_cancel = (Button)findViewById(R.id.button_cancel);
        button_reset = (Button)findViewById(R.id.button_reset);
        button_post = (Button)findViewById(R.id.button_post);

        button_cancel.setOnClickListener(this);
        button_reset.setOnClickListener(this);
        button_post.setOnClickListener(this);
        imageView_ticketPic.setOnClickListener(this);
        arguments = getIntent().getBundleExtra(AppUtils.MAIN_BUNDLE);
        ArrayList<String> tempList = arguments.getStringArrayList(AppUtils.EVENT_NAMES);
        eventNames = new ArrayList<String>();
        eventNames.add(DEFAULT_SPINNER_TEXT);
        eventNames.addAll(tempList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner_eventName.setAdapter(adapter);
        spinner_eventName.setOnItemSelectedListener(this);

        chosenEventName=null;
        imageNull = true;



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__sell_ticket, menu);
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
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_cancel:
                this.finish();
                break;
            case R.id.button_reset:
                clearFields();
                break;
            case R.id.button_post:
                postTicket();
                break;
            case R.id.imageView_ticketPic:
                launchCamera();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get(DATA_KEY);
            imageView_ticketPic.setImageBitmap(imageBitmap);
            imageNull = false;
            imageData = imageBitmap;
        }



    }

    public void launchCamera(){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    public void clearFields(){

        editText_description.setText("");
        editText_price.setText("");
        imageView_ticketPic.setImageResource(R.drawable.click_to_take_a_pic);
        imageNull = true;
    }

    public void postTicket(){
        if(imageNull){
            AppUtils.toast(this,IMAGE_NULL);
            return;
        }
        if(chosenEventName == null){
            AppUtils.toast(this,NAME_NULL);
            return;
        }
        if(editText_description.getText().toString().isEmpty()){
            AppUtils.toast(this,DESC_NULL);
            return;
        }else{
            description = editText_description.getText().toString().trim();
        }
        if(editText_price.getText().toString().isEmpty()){
            AppUtils.toast(this,PRICE_NULL);
            return;
        }else{
            price = editText_price.getText().toString().trim();
        }
        if(editTexT_contact.getText().toString().isEmpty()){
            AppUtils.toast(this,CONTACT_NULL);
            return;
        }else{
            contact = editTexT_contact.getText().toString().trim();
        }

        Intent result = new Intent();
        result.putExtra(NAME_KEY,chosenEventName);
        result.putExtra(DESC_KEY,description);
        result.putExtra(PRICE_KEY,price);
        result.putExtra(IMAGE_KEY,imageData);
        result.putExtra(CONTACT_KEY,contact);
        this.setResult(RESULT_OK,result);
        imageNull = true;
        chosenEventName = null;
        this.finish();







    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenEventName = (String) parent.getItemAtPosition(position);
        if(chosenEventName.equals(DEFAULT_SPINNER_TEXT)){
            chosenEventName =null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        chosenEventName =null;

    }
}
