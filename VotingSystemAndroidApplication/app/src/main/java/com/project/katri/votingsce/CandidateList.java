package com.project.katri.votingsce;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CandidateList extends AppCompatActivity implements AsyncCandidateResponse{
    private User CurrentUser;
    private int flag;
    private DrawerLayout mDrawerLayout;
    String ExistingVote;
    ArrayList<Candidate> candidateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_list);

        Intent intent = getIntent();
        ExistingVote = intent.getStringExtra("VoteNum");

        candidateList = new ArrayList<>();

        try{ CurrentUser = CurrentUser.getInstance();}
        catch (AssertionError error){
            Intent intent2 = new Intent(this,Login.class);
            startActivity(intent2);
            finish();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        CandidateSync cs = new CandidateSync(this, ExistingVote);
        cs.delegate = this;
        cs.execute();
    }


    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @Override
    public void processFinish(ArrayList<Candidate> candidateList) {
        this.candidateList = candidateList;
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        VotingList.Adapter adapter = new VotingList.Adapter(getSupportFragmentManager());
        VotingListFragment votings = new VotingListFragment();
        votings.setCandidateList(candidateList,false,ExistingVote,this);
        adapter.addFragment(votings, "");
        viewPager.setAdapter(adapter);
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
        Intent intent = new Intent(CandidateList.this,VotingList.class);
        intent.putExtra("flag", "0");
        startActivity(intent);
        finish();
    }

    public void onBackClick(View arg){
        Intent intent = new Intent(CandidateList.this,VotingList.class);
        intent.putExtra("flag", "0");
        startActivity(intent);
        finish();
    }

    public void LogOff(final View arg0) {
        CurrentUser.Reset();
        this.finishAffinity();
        Intent intent = new Intent(CandidateList.this,Login.class);
        startActivity(intent);
        finish();
    }
}
