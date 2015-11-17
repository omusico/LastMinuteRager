package com.boomer.omer.lastminuterager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.boomer.omer.lastminuterager.AppUtils;
import com.boomer.omer.lastminuterager.R;


public class Activity_SignUp extends Activity implements View.OnClickListener{


    //UI DECLARE
    EditText editText_userName;
    EditText editText_passwordFirst;
    EditText editText_passwordSecond;

    CheckBox checkBox_terms;

    Button button_signUp;
    Button button_cancel;
    Button button_showTerms;

    String userName;
    String passwordFirst;
    String passwordSecond;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        //UI INIT
        editText_userName=(EditText)findViewById(R.id.editText_username);
        editText_passwordFirst=(EditText)findViewById(R.id.editText_passwordFirst);
        editText_passwordSecond=(EditText)findViewById(R.id.editText_passwordSecond);
        button_signUp = (Button)findViewById(R.id.button_signup);
        button_cancel = (Button)findViewById(R.id.button_cancel);
        button_showTerms = (Button)findViewById(R.id.button_showTerms);

        checkBox_terms = (CheckBox)findViewById(R.id.checkBox_acceptTerms);

        button_signUp.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
        button_showTerms.setOnClickListener(this);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_signup:

                passwordFirst = editText_passwordFirst.getText().toString();
                passwordSecond = editText_passwordSecond.getText().toString();
                userName = editText_userName.getText().toString().trim();

                if(!passwordFirst.equals(passwordSecond)){
                    AppUtils.toast(getApplicationContext(), "Passwords do not match");

                    break;
                     }

                if(!checkBox_terms.isChecked()) {
                        AppUtils.toast(getApplicationContext(),"Terms have not been accepted");

                        break;
                    }

                AppUtils.parseSignUp(userName,passwordFirst,passwordSecond,this);






                break;

            case R.id.button_cancel:
               this.finish();

                break;

            case R.id.button_showTerms:
                showTerms();

                break;
        }

    }


    public void showTerms(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.terms_and_conditions)
                .setTitle(R.string.terms_and_conditions_title);


        builder.setPositiveButton(R.string.string_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();



    }
}
