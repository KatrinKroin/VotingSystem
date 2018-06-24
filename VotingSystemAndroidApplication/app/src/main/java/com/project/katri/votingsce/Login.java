package com.project.katri.votingsce;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private User CurrentUser;
    private EditText Email;
    private EditText Password;
    private FloatingActionButton fab, fab1, fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    public Button login;
    private static final int READ_REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        CurrentUser.Reset();

        Email = (EditText) findViewById(R.id.Email);
        Email.setTextColor(Color.WHITE);
        Password = (EditText) findViewById(R.id.Password);
        Password.setTextColor(Color.WHITE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimateFab();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimateFab();
                Toast.makeText(getApplicationContext(),"Not available yet!",Toast.LENGTH_LONG).show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimateFab();
                Toast.makeText(getApplicationContext(),"Not available yet!",Toast.LENGTH_LONG).show();
            }
        });

        login = (Button) findViewById(R.id.Login);

        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        //    login.setClickable(true);
        //}
        //else{
        //    ActivityCompat.requestPermissions((Activity) this,new String[]{Manifest.permission.READ_PHONE_STATE},READ_REQUEST_CODE );
        //}

    }


    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case READ_REQUEST_CODE:
                // Check Permissions Granted or not
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    login.setClickable(true);
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Phone state permission denied, you can't login before you allow the permission.", Toast.LENGTH_SHORT).show();
                    login.setClickable(false);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
     }
    */


/*
        if( RC == READ_REQUEST_CODE){
            Toast.makeText((Activity) this,"OK.", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText((Activity) this,"Permission canceled, now your application cannot access PHONE STATE.", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */

    private  void AnimateFab(){
        if(isOpen){
            fab.startAnimation(rotateForward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen = false;
        }
        else{
            fab.startAnimation(rotateBackward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen = true;
        }
    }

    public void TermsAndConditions(View view){
        Intent intent = new Intent(this,TermsAndConditions.class);
        startActivity(intent);
    }

    public void checkLogin(final View arg0) {
        if((Email.getText().toString()).equals("") || (Password.getText().toString()).equals("")){
            Toast.makeText(getApplicationContext(), "There are missing details!", Toast.LENGTH_LONG).show();
        }
        else if(!isValidEmail(Email.getText()) && !isValidPhone(Email.getText().toString())){
            Toast.makeText(getApplicationContext(), "Illegal username!", Toast.LENGTH_LONG).show();
        }
        else {
            UserSync us = new UserSync(this);
            us.execute();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public final static boolean isValidPhone(String target) {
        String regex = "[0-9]+";
        return target.matches(regex);
    }

}
