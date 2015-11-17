package com.boomer.omer.lastminuterager.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;
import com.boomer.omer.lastminuterager.backendprocesses.PostTicket;
import com.boomer.omer.lastminuterager.fragments.FragmentController;
import com.boomer.omer.lastminuterager.fragments.MainPageChangeAdapter;
import com.boomer.omer.lastminuterager.fragments.MainPagerAdapter;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class Activity_Main extends ActionBarActivity implements View.OnClickListener, android.support.v7.app.ActionBar.TabListener, FragmentController{


    public String loginResult;
    public Bundle mainBundle;
    MainPagerAdapter mainPagerAdapter;

    public static String FILE_EVENT_NAMES = "eventNames";
    File file_eventNames;

    //INIT TABS
    ViewPager viewPager;
    android.support.v7.app.ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        file_eventNames = new File(this.getCacheDir(),FILE_EVENT_NAMES);



        //SCROLLABLE SCREEN DECLARATIONS
       viewPager= (ViewPager)findViewById(R.id.pager_main);
        actionBar = getSupportActionBar();
       actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);
       FragmentManager fragmentManager = getSupportFragmentManager();
       mainPagerAdapter = new MainPagerAdapter(fragmentManager);
        viewPager.setAdapter(mainPagerAdapter);
       MainPageChangeAdapter mainPageChangeAdapter = new MainPageChangeAdapter(actionBar);
       viewPager.setOnPageChangeListener(mainPageChangeAdapter);






        //CREATE TABS
        android.support.v7.app.ActionBar.Tab userTab = actionBar.newTab();
        userTab.setText(AppUtils.TAB_USER_PROFILE);
        userTab.setTabListener((android.support.v7.app.ActionBar.TabListener) this);
        android.support.v7.app.ActionBar.Tab eventsTab = actionBar.newTab();
        eventsTab.setText(AppUtils.TAB_EVENTS);
        eventsTab.setTabListener((android.support.v7.app.ActionBar.TabListener) this);
        android.support.v7.app.ActionBar.Tab ticketsTab = actionBar.newTab();
        ticketsTab.setText(AppUtils.TAB_TICKETS);
        ticketsTab.setTabListener((android.support.v7.app.ActionBar.TabListener) this);

        actionBar.addTab(userTab);
        actionBar.addTab(eventsTab);
        actionBar.addTab(ticketsTab);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_logout){
            startLogout(this);
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onResume() {
        super.onResume();
        ParseUser currentUser = ParseUser.getCurrentUser();
        //currentUser.logOut();
        if(currentUser == null){

            startLogin();


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_CANCELED && requestCode == AppUtils.LOGIN_REQUEST){
            this.finish();
        }

        if(resultCode == RESULT_OK){
            loginResult = data.getStringExtra(AppUtils.LOGIN_RESULT);
            if (requestCode == AppUtils.LOGIN_REQUEST){
                AppUtils.toast(getApplicationContext(),loginResult);

            }
            if(requestCode == AppUtils.TICKET_REQUEST){
                PostTicket postTicket = new PostTicket(this,data);
                postTicket.execute();

            }

        }
    }

    @Override
    public void onClick(View v) {



    }

    public void startLogin(){
        Intent intent = new Intent(
                Activity_Main.this,
                Activity_LogIn.class);
        startActivityForResult(intent, AppUtils.LOGIN_REQUEST);

    }


    public void launchSellActivity(){
        Intent intent = new Intent(
                Activity_Main.this,
                Activity_SellTicket.class);
        intent.putExtra(AppUtils.MAIN_BUNDLE,mainBundle);
        startActivityForResult(intent, AppUtils.TICKET_REQUEST);
    }





    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void pressedButton(int buttonID) {
        switch (buttonID){
            case R.id.button_sell:
                launchSellActivity();
                break;



        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        file_eventNames.delete();
    }

    @Override
    public void sendData(Bundle bundle) {

        mainBundle = bundle;
        try {
            file_eventNames.delete();
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(FILE_EVENT_NAMES,MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            oos.writeObject(bundle.getStringArrayList(AppUtils.EVENT_NAMES));
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(AppUtils.TAG, e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(AppUtils.TAG, e.toString());
        }

        //mainPagerAdapter.getItem(2).setArguments(bundle);
    }

    public void startLogout(final Context context){
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Log Out?");
        dialogBuilder.setMessage("You will be taken to the Log In screen.");
        dialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppUtils.parseLogOut(context);
                startLogin();
            }
        });
        dialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
}
