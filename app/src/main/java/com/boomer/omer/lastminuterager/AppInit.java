package com.boomer.omer.lastminuterager;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.FacebookSdkNotInitializedException;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by Omer on 8/11/2015.
 */
public class AppInit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppUtils.ParseRegister(this);
        AppUtils.FacebookRegister(this);
        AppUtils.TwitterRegister(this);




    }
}
