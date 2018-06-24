package com.project.katri.votingsce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.view.ViewGroup.LayoutParams;

/**
 * Created by Katri on 22/02/2018.
 */

public class VotingListFragment extends Fragment {
    public static ArrayList<Voting> UserVotings;
    public static ArrayList<Candidate> Candidates;
    public boolean showVotings = true;
    public String existingVote;
    public String flag = "0";
    public static Activity activity;
    public static AlertDialog.Builder builder;
    public static AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate( R.layout.fragment_voting_list, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), showVotings, flag,existingVote));
    }

    public void setVotingList(ArrayList<Voting> UserVotings, boolean showVotings, String flag, Activity activity){
        if(UserVotings != null)
            this.UserVotings = UserVotings;
        else this.UserVotings = new ArrayList<>();
        this.showVotings = showVotings;
        this.flag = flag;
        this.activity = activity;
    }

    public void setCandidateList(ArrayList<Candidate> Candidates, boolean showVotings,String existingVote,Activity activity){
        if(Candidates != null)
            this.Candidates = Candidates;
        else this.Candidates = new ArrayList<>();
        this.existingVote = existingVote;
        this.showVotings = showVotings;
        this.flag = flag;
        this.activity = activity;
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<String> mNames;
        private List<String> mDescriptions;
        private List<Date> startDates;
        private List<Date> finishDates;
        public boolean showVotings = true;
        public String existingVote;
        public String flag = "0";
        //public Activity activity;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;
            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            public final TextView mTextView2;
            public final TextView mTimer;
            public final LinearLayout listelement;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTimer = (TextView) view.findViewById(R.id.timer);
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(R.id.text1);
                mTextView2 = (TextView) view.findViewById(R.id.text2);
                listelement =  (LinearLayout) view.findViewById(R.id.container);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, boolean showVotings, String flag, String existingVote) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;

            //this.UserVotings = UserVotings;
            //this.Candidates = Candidates;
            this.showVotings = showVotings;
            this.flag = flag;
            this.existingVote = existingVote;

            if(showVotings == true){
                mNames = getVotingsNames();
                mDescriptions = getVotingsDescriptions();
                startDates = getVotingsStartDates();
                finishDates = getVotingsFinishDates();
                //this.activity = activity;
            }
            else{
                mNames = getCandidatesNames();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            if(showVotings == true){
                holder.mBoundString = mNames.get(position);
                holder.mTextView.setText(mNames.get(position));

                holder.mBoundString = mDescriptions.get(position);
                holder.mTextView2.setText(mDescriptions.get(position));
                if(flag.equals("0")) {
                    runTimer(holder.mTimer, holder.mImageView, position);
                }
            }
            else {
                holder.mBoundString = mNames.get(position);
                holder.mTextView2.setText(mNames.get(position));
                LayoutParams p = holder.mTextView.getLayoutParams();
                p.height = 0;
                holder.mTextView.setLayoutParams(p);
                LayoutParams p2 = holder.mTimer.getLayoutParams();
                p2.height = 0;
                holder.mTimer.setLayoutParams(p2);
                LayoutParams p3 = holder.mTextView2.getLayoutParams();
                p3.height = holder.listelement.getLayoutParams().height;
                holder.mTextView2.setLayoutParams(p3);
                //holder.mTextView2.setBackgroundColor(activity.getResources().getColor(R.color.green));
                //holder.mTextView2.setTextSize(21);
                holder.mTextView2.setTypeface(null, Typeface.BOLD);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    if(showVotings == true){
                        if(flag.equals("0")){
                            if(Voting.availableVoting(finishDates.get(position), startDates.get(position))){
                                Intent intent = new Intent(context, CandidateList.class);
                                intent.putExtra("flag", "0");
                                intent.putExtra("VoteNum", Integer.toString(UserVotings.get(position).getVoteNum()));
                                context.startActivity(intent);
                                //this.finish();

                            }
                            else{
                                holder.itemView.setEnabled(false);
                            }
                        }
                        else {
                            Intent intent = new Intent(context, Results.class);
                            intent.putExtra("VoteNum", Integer.toString(UserVotings.get(position).getVoteNum()));
                            context.startActivity(intent);
                        }
                    }
                    else{
                        if (!((dialog != null) && dialog.isShowing())) {
                            builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
                            dialog = builder
                                    .setMessage(Html.fromHtml("<font color='#e4e5e7'>       You selected: " + Candidates.get(position).getCandidateName() + ".<br>       Are you sure in your choice?</font>"))
                                    .setNegativeButton("No", null)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SendBalot(0, existingVote, Candidates.get(position).getCandidateID(), context);
                                        }
                                    }).create();
                            dialog.show();
                        }
                    }
                }
            });

            if(showVotings == true){
                if(Voting.availableVoting(finishDates.get(position),startDates.get(position)))
                    Glide.with(holder.mImageView.getContext()).load(Voting.getDrawable(0)).fitCenter().into(holder.mImageView);
                else if(Voting.featureVoting(startDates.get(position)))
                    Glide.with(holder.mImageView.getContext()).load(Voting.getDrawable(2)).fitCenter().into(holder.mImageView);
                else
                    Glide.with(holder.mImageView.getContext()).load(Voting.getDrawable(1)).fitCenter().into(holder.mImageView);
            }
            else{
                if(existingVote.equals("100")){
                    if(mNames.get(position).equals("Green Team"))Glide.with(holder.mImageView.getContext()).load(Voting.getDrawable(3)).fitCenter().into(holder.mImageView);
                    else if(mNames.get(position).equals("Red Team"))Glide.with(holder.mImageView.getContext()).load(Voting.getDrawable(4)).fitCenter().into(holder.mImageView);
                    else Glide.with(holder.mImageView.getContext()).load(Voting.getDrawable(5)).fitCenter().into(holder.mImageView);
                }
                else Glide.with(holder.mImageView.getContext()).load(Voting.getDrawable(7)).fitCenter().into(holder.mImageView);
            }
        }

        public void runTimer(final TextView mTimer, final ImageView mImageView, final int position){
            final Handler timerHendler = new Handler();
            final Runnable timerRunnable = new Runnable() {
                @SuppressLint({"ResourceAsColor", "DefaultLocale"})
                @Override
                public void run() {
                    long millisecondToFinish;
                    String msg = "";
                    if (Voting.availableVoting(finishDates.get(position), startDates.get(position))) {
                        millisecondToFinish = finishDates.get(position).getTime() - (System.currentTimeMillis()); // + 7200000
                        mTimer.setTextColor(Color.parseColor("#FF6FA636"));
                        msg = "Closes in: ";

                        long allseconds = (long) millisecondToFinish / 1000;
                        long secToFinish = allseconds % 60;
                        long minTiFinish = (allseconds / 60) % 60;
                        long hoursToFinish = (allseconds / 3600) % 24; //*********************************
                        long daysToFinish = (allseconds / 86400);
                        mTimer.setText(String.format("%s %d days, %02d:%02d:%02d", msg, daysToFinish, hoursToFinish, minTiFinish, secToFinish));
                        timerHendler.postDelayed(this, 500);

                    } else if(Voting.featureVoting(startDates.get(position))){
                        millisecondToFinish = startDates.get(position).getTime() - (System.currentTimeMillis()); // + 7200000
                        mTimer.setTextColor(Color.parseColor("#71baff"));  // a6121b
                        msg = "Starts in: ";

                        long allseconds = (long) millisecondToFinish / 1000;
                        long secToFinish = allseconds % 60;
                        long minTiFinish = (allseconds / 60) % 60;
                        long hoursToFinish = (allseconds / 3600) % 24; //*********************************
                        long daysToFinish = (allseconds / 86400);
                        mTimer.setText(String.format("%s %d days, %02d:%02d:%02d", msg, daysToFinish, hoursToFinish, minTiFinish, secToFinish));
                        timerHendler.postDelayed(this, 500);

                    }
                    else{
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        activity.finish();
                        activity.startActivity(activity.getIntent());
                        //millisecondToFinish = 0;
                        //mTimer.setTextColor(Color.parseColor("#a6121b"));
                        //mTimer.setText(String.format("   Closed"));

                       // Glide.with(mImageView.getContext()).load(Voting.getDrawable(1)).fitCenter().into(mImageView);

                    }
                }
            };
            timerHendler.postDelayed(timerRunnable, 0);
        }

        @Override
        public int getItemCount() {
            return mNames.size();
        }

        private void SendBalot(int flag,String existingVote, String candidateID,Context context){
            if(flag == 0){
                SendBalotSync balot = new SendBalotSync(context,flag ,existingVote ,candidateID);
                balot.execute();
            }
        }

        private List<String> getVotingsNames(){
            ArrayList<String> list = new ArrayList<>(UserVotings.size());
            for(int i=0;i<UserVotings.size();i++){
                list.add(UserVotings.get(i).getVoteName());
            }
            return list;
        }

        private List<String> getCandidatesNames(){
            ArrayList<String> list = new ArrayList<>(Candidates.size());
            for(int i=0;i<Candidates.size();i++){
                list.add(Candidates.get(i).getCandidateName());
            }
            return list;
        }

        private List<String> getVotingsDescriptions(){
            ArrayList<String> list = new ArrayList<>(UserVotings.size());
            for(int i=0;i<UserVotings.size();i++){
                list.add(UserVotings.get(i).getVoteDescription());
            }
            return list;
        }

        private List<Date> getVotingsStartDates(){
            ArrayList<Date> list = new ArrayList<>(UserVotings.size());
            for(int i=0;i<UserVotings.size();i++){
                list.add(UserVotings.get(i).getStart());
            }
            return list;
        }

        private List<Date> getVotingsFinishDates(){
            ArrayList<Date> list = new ArrayList<>(UserVotings.size());
            for(int i=0;i<UserVotings.size();i++){
                list.add(UserVotings.get(i).getFinish());
            }
            return list;
        }
    }
    @Override
    public void onPause(){
        if ((dialog != null) && dialog.isShowing()) dialog.dismiss();
        activity.finish();
        super.onPause();
    }
}
