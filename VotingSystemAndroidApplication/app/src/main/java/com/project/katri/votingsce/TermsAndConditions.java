package com.project.katri.votingsce;

import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TermsAndConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

       // NestedScrollView nestedscroll = (NestedScrollView) findViewById(R.id.nestedscroll);

    }

    public void Close(View arg){
        this.finish();
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this,"You must complete reading all the terms and conditions!",Toast.LENGTH_LONG).show();
    }
}
