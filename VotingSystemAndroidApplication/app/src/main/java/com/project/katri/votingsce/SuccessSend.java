package com.project.katri.votingsce;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessSend extends AppCompatActivity {
    private User CurrentUser;
    public TextView data;
    public TextView results;
    private String result;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_send);

        Intent intent = getIntent();
        result = intent.getStringExtra("result");

        try{ CurrentUser = CurrentUser.getInstance();}
        catch (AssertionError error){
            Intent intent2 = new Intent(this,Login.class);
            startActivity(intent2);
            finish();
        }

        results = (TextView) findViewById(R.id.thankyou);
        image = (ImageView) findViewById(R.id.resultImg);
        data = (TextView) findViewById(R.id.hellouser);
        data.setText("Dear " + CurrentUser.GetUserName() +"!");
        if(!result.equals("true")) displayResultsToUser();
    }

    public void displayResultsToUser(){
        image.setImageResource(R.drawable.x);
        switch (result){
            case "false": results.setText("Seems like you already voted in this election...");
                break;
            case "Timeout": results.setText("This election is already closed!");
                break;
            default: results.setText("Something went wrong, please try again later...");
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void OtherVotings(final View arg0) {
        Intent intent = new Intent(SuccessSend.this,VotingList.class);
        this.finishAffinity();
        intent.putExtra("flag", "0");
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Logoff(final View arg0) {
        CurrentUser.Reset();
        Intent intent = new Intent(SuccessSend.this,Login.class);
        this.finishAffinity();
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(SuccessSend.this,VotingList.class);
        intent.putExtra("flag", "0");
        startActivity(intent);
        finish();
    }

    public void onBackClick(View arg){
        Intent intent = new Intent(SuccessSend.this,VotingList.class);
        intent.putExtra("flag", "0");
        startActivity(intent);
        finish();
    }

    public void LogOff(final View arg0) {
        CurrentUser.Reset();
        this.finishAffinity();
        Intent intent = new Intent(SuccessSend.this,Login.class);
        startActivity(intent);
        finish();
    }
}
