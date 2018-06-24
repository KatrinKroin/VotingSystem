package com.project.katri.votingsce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.iwgang.countdownview.CountdownView;

public class VotingList extends AppCompatActivity implements AsyncResponse {
    private User CurrentUser;
    public ArrayList<Voting> voteList;
    private int flag;
    private DrawerLayout mDrawerLayout;
    private String admin;
    private TextView msg;
    private ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_list);

        voteList = new ArrayList<>();

        try{ CurrentUser = CurrentUser.getInstance();}
        catch (AssertionError error){
            //Toast.makeText(getApplicationContext(), "Error: the user doesn't exist!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
        }

        Intent intent = getIntent();
        admin = intent.getStringExtra("flag");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        arrow = (ImageView) findViewById(R.id.backarrow);
        if(admin.equals("0")) arrow.setVisibility(View.INVISIBLE);

        msg = (TextView) findViewById(R.id.msg);

        VotingSync vs;
        if(admin.equals("0")) vs = new VotingSync(this,0);
        else vs = new VotingSync(this,1);
        vs.delegate = this;
        vs.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(CurrentUser.GerStatus() == true && admin.equals("0")) fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CurrentUser.GerStatus() == true){
                    Intent intent = new Intent(VotingList.this,VotingList.class);
                    intent.putExtra("flag", "1");
                    startActivity(intent);
                }else{
                    Snackbar.make(view, "You don't have permission to view voting results.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }


    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        VotingListFragment votings = new VotingListFragment();
        votings.setVotingList(voteList,true,admin, this);
        adapter.addFragment(votings, "");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void processFinish(ArrayList<Voting> voteList, int flag) {
        this.voteList = voteList;
        this.flag = flag;

        if(this.voteList == null) onRestart();
        else if(this.voteList.size() == 0) msg.setVisibility(View.VISIBLE);
        else{
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            if (viewPager != null) setupViewPager(viewPager);
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onBackPressed(){
        if(admin.equals("1") ){
            Intent intent = new Intent(VotingList.this,VotingList.class);
            intent.putExtra("flag", "0");
            startActivity(intent);
            finish();
        }
        else{
            CurrentUser.Reset();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
        }
    }

    public void onBackClick(View arg){
        onBackPressed();
    }

    public void LogOff(final View arg0) {
        CurrentUser.Reset();
        this.finishAffinity();
        Intent intent = new Intent(VotingList.this,Login.class);
        startActivity(intent);
        finish();
    }
}
