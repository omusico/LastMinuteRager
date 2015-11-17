package com.boomer.omer.lastminuterager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;
import com.parse.ParseFacebookUtils;


public class Activity_LogIn extends Activity implements View.OnClickListener{

    //UI DECLARE
    ImageView imageView_intro;

    EditText editText_userName;
    EditText editText_password;

    Button button_login;
    Button button_signUp;
    Button button_facebook_login;
    Button button_twitter_login;

    String userName;
    String password;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        //UI INIT
        imageView_intro = (ImageView)findViewById(R.id.imageView_intro);


        editText_userName = (EditText)findViewById(R.id.editText_username);
        editText_password = (EditText)findViewById(R.id.editText_password);
        button_login = (Button)findViewById(R.id.button_login);
        button_facebook_login = (Button)findViewById(R.id.button_facebook);
        button_twitter_login = (Button)findViewById(R.id.button_twitter);
        button_signUp = (Button)findViewById(R.id.button_signup);

        button_login.setOnClickListener(this);
        button_facebook_login.setOnClickListener(this);
        button_twitter_login.setOnClickListener(this);
        button_signUp.setOnClickListener(this);
    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();




        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_login: //AUTHENTICATE PARSE
                userName = editText_userName.getText().toString().trim();
                password = editText_password.getText().toString().trim();
                AppUtils.parseLogin(this, userName, password);
                break;
            case R.id.button_signup: //LAUNCH SIGN UP ACTIVITY
                Intent intent = new Intent(
                        Activity_LogIn.this,
                        Activity_SignUp.class);
                startActivity(intent);
                break;
            case R.id.button_facebook:

                AppUtils.facebookLogin(this);
                break;
            case R.id.button_twitter:

                AppUtils.twitterLogin(this);
        }

    }
}
