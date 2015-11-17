package com.boomer.omer.lastminuterager;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.boomer.omer.lastminuterager.backendprocesses.DLEvents;
import com.boomer.omer.lastminuterager.backendprocesses.DLNotifications;
import com.boomer.omer.lastminuterager.backendprocesses.DLTickets;
import com.facebook.FacebookSdk;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Omer on 8/11/2015.
 */
public class AppUtils {

    public static final int NOTIFICATION_ID = 101;

    public static final int SHORT_TOAST = Toast.LENGTH_SHORT;
    public static final int LONG_TOAST = Toast.LENGTH_SHORT;

    public static final String MAIN_BUNDLE = "mainBundle";
    public static final String EVENT_NAMES = "eventNames";

    public static final String TAG = "LMR:";

    public static final String PARSE_LOGIN = "Logged in...";
    public static final String FACEBOOK_LOGIN = "Facebook logged in...";
    public static final String TWITTER_LOGIN = "Twitter logged in...";
    public static final String PARSE_SIGN_UP ="Signed up...";

    public static final String TICKET_POSTING = "Posting your ticket...";
    public static final String TICKET_POSTED = "Your ticket has been posted...";

    public static final int TICKET_REQUEST = 2;
    public static final String TICKET_RESULT = "ticketResult";

    public static final int SIGN_UP_SUCCESS = 1;

    public static final int LOGIN_REQUEST = 1;
    public static final String LOGIN_RESULT = "result";

    public static final String TAB_USER_PROFILE = "Profile";
    public static final String TAB_EVENTS = "Events";
    public static final String TAB_TICKETS = "Tickets";

    public static final String IE_COLUMN = "interestedEventIDs";

    public static final String USER_ID = "userID";

    public static final String CHANNELS = "channels";


    public static void ParseRegister(Context context){
        Parse.enableLocalDatastore(context);
        Parse.initialize(context,context.getResources().getString(R.string.parse_API),context.getResources().getString(R.string.parse_KEY));
        ParseUser.enableRevocableSessionInBackground();
        ParseInstallation.getCurrentInstallation().saveInBackground();



    }

    public static void FacebookRegister(Context context){
        FacebookSdk.sdkInitialize(context);
        ParseFacebookUtils.initialize(context);
    }

    public static void TwitterRegister(Context context){
        ParseTwitterUtils.initialize(context.getResources().getString(R.string.twitter_KEY),context.getResources().getString(R.string.twitter_SECRET));


    }

    public static void parseSignUp(String userName, String passwordFirst, String passwordSecond,final Activity activity){

        ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setPassword(passwordFirst);
        newUser.setEmail(userName+"@test.com");
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {

                    AppUtils.toast(activity,"SUCCESS");
                    activity.finish();
                } else {
                    if(e.getCode() == com.parse.ParseException.INVALID_SESSION_TOKEN){
                        AppUtils.toast(activity,"Session error fixed! Try signing up now!");
                        ParseUser.getCurrentUser().logOut();


                    }
                    AppUtils.log(e.toString());
                    AppUtils.toast(activity,e.toString());


                }
            }

        });


    }



    public static void toast(Context context, String string, int length){
        Toast.makeText(context,string,length).show();

    }
    public static void toast(Context context, String string){
        Toast.makeText(context,string,LONG_TOAST).show();

    }

    public static void log(String string){
        Log.d(TAG,string);

    }

    public static void facebookLogin(final Activity activity ){


            final Dialog progressDialog;

            List<String> permissions = Arrays.asList("public_profile", "email");


            ParseFacebookUtils.logInWithReadPermissionsInBackground(activity, permissions, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException err) {

                    if (user == null) {
                        AppUtils.toast(activity,"Facebook login failed...");
                    } else {
                        if(user.isNew()){
                            newUser();

                        }
                        subscribeChannels(user);

                        Intent result = new Intent();
                        result.putExtra(AppUtils.LOGIN_RESULT,AppUtils.FACEBOOK_LOGIN);

                        activity.setResult(activity.RESULT_OK, result);
                        activity.finish();



                    }
                }
            });



    }

    public static void parseLogin(final Activity activity,String userName, String password){
        ParseUser.logInInBackground(userName, password,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user,  com.parse.ParseException e) {
                        if (user != null) {
                            if(user.isNew()){
                                newUser();

                            }

                            subscribeChannels(user);
                            Intent result = new Intent();
                            result.putExtra(AppUtils.LOGIN_RESULT,AppUtils.PARSE_LOGIN);
                            activity.setResult(activity.RESULT_OK, result);
                            activity.finish();
                        } else {
                            AppUtils.toast(activity.getApplicationContext(),"Authentication failed, try again...");

                        }
                    }
                });


    }

    public static void twitterLogin(final Activity activity){
        ParseTwitterUtils.logIn(activity, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    AppUtils.toast(activity.getApplicationContext(),"Authentication failed, try again...");
                } else {
                    if(user.isNew()){
                        newUser();

                    }
                    subscribeChannels(user);
                    Intent result = new Intent();
                    result.putExtra(AppUtils.LOGIN_RESULT,AppUtils.TWITTER_LOGIN);

                    activity.setResult(activity.RESULT_OK, result);
                    activity.finish();
                }
            }
        });

    }

    public static void newUser()    {

        ParseUser temp = ParseUser.getCurrentUser();
        temp.add(IE_COLUMN,null);
        temp.saveInBackground();

    }

    public static void parseLogOut(Context context){
        unSubscribeChannels();
        ParseUser.getCurrentUser().logOut();
        AppUtils.toast(context,"Logged out!");
    }

    public static void subscribeChannels(final ParseUser user){
        /*
        final Thread subscribe = new Thread(new Runnable() {
            @Override
            public void run() {
                ParseUser currentUser = user;
                ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                List<Object> followedIDs = new ArrayList<>();
                followedIDs = user.getList(IE_COLUMN);


                for(Object _object:followedIDs){
                 // AppUtils.log((String)_object);
                    parseInstallation.add(CHANNELS,_object);
                }

                parseInstallation.put(USER_ID, ParseUser.getCurrentUser().getObjectId());


                try {
                    parseInstallation.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });
        subscribe.start();
        */
        //ParsePush.subscribeInBackground(user.getObjectId());

    }

    public static void unSubscribeChannels(){
        /*
        Thread unsubscribe = new Thread(new Runnable() {
            @Override
            public void run() {
                ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
                List<Object> followedIDs = new ArrayList<>();
                followedIDs.clear();
                parseInstallation.put(CHANNELS,followedIDs);
                try {
                    parseInstallation.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                try {
                    parseInstallation.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        unsubscribe.start();
        */


    }

}
