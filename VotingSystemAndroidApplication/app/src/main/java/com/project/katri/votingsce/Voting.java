package com.project.katri.votingsce;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Katri on 22/02/2018.
 */

public class Voting {
    private int VoteNum;
    private String VoteName;
    private String VoteDescription;
    private Date Start;
    private Date Finish;

    public Voting(){}

    public Voting(int VoteNum, String VoteName, String VoteDescription, String Start, String Finish) throws ParseException {
        this.VoteNum = VoteNum;
        this.VoteName = VoteName;
        this.VoteDescription = VoteDescription;
        this.Start = Parse( Start );
        this.Finish = Parse(Finish);

    }

    public int getVoteNum() {
        return VoteNum;
    }

    public String getVoteName() {
        return VoteName;
    }

    public void setVoteName(String voteName) {
        VoteName = voteName;
    }

    public String getVoteDescription() {
        return VoteDescription;
    }

    public void setVoteDescription(String voteDescription) {
        VoteDescription = voteDescription;
    }

    public Date getStart() {
        return Start;
    }

    public void setStart(Date start) {
        this.Start = start;
    }

    public Date getFinish() {
        return Finish;
    }

    public void setFinish(Date finish) {
        this.Finish = finish;

    }

    public static int getDrawable(int i) {
        switch (i) {
            case 0:
                return R.drawable.sceopen;
            case 1:
                return R.drawable.sceclosed;
            case 2:
                return R.drawable.scefuture;
            case 3:
                return R.drawable.greenteam;
            case 4:
                return R.drawable.readteam;
            case 5:
                return R.drawable.purpleteam;
            default:
                return R.drawable.candidate;
        }
    }

    public static Date Parse( String input ) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(input.replaceAll("Z$", "+0000"));

        return date;
    }

    public static String toString( Date date ) {

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );

        TimeZone tz = TimeZone.getTimeZone( "UTC" );

        df.setTimeZone( tz );

        String output = df.format( date );

        int inset0 = 9;
        int inset1 = 6;

        String s0 = output.substring( 0, output.length() - inset0 );
        String s1 = output.substring( output.length() - inset1, output.length() );

        String result = s0 + s1;

        result = result.replaceAll( "UTC", "+00:00" );

        return result;

    }

    public static boolean Compare( String start , String finish) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current = formatter.format(new Date());
            //Date date = new Date(new Date().getTime()+ 7200000);
            //String current = formatter.format(date);


            Date s = formatter.parse(start.replaceAll("Z$", "+0000"));
            Date f = formatter.parse(finish.replaceAll("Z$", "+0000"));
            Date c = formatter.parse(current.replaceAll("Z$", "+0000"));
            if (f.compareTo(c)>0) { //&& c.compareTo(s)>0 || f.compareTo(c)<0 && s.compareTo(c)>0
                return true;
            }
        }
        catch (ParseException e)
        {
            return false;
        }
        return false;
    }

    public static boolean availableVoting(Date f, Date s) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current = formatter.format(new Date());
            //Date date = new Date(new Date().getTime()+ 7200000);
            //String current = formatter.format(date);

            //Date s = formatter.parse(start.replaceAll("Z$", "+0000"));
            //Date f = formatter.parse(finish.replaceAll("Z$", "+0000"));
            Date c = formatter.parse(current.replaceAll("Z$", "+0000"));
            if (f.compareTo(c)>0 && c.compareTo(s)>0 ){
                return true;
            }
        }
        catch (ParseException e)
        {
            return false;
        }
        return false;
    }

    public static boolean featureVoting(Date s) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String current = formatter.format(new Date());
            //Date date = new Date(new Date().getTime()+ 7200000);
            //String current = formatter.format(date);

            Date c = formatter.parse(current.replaceAll("Z$", "+0000"));
            if (s.compareTo(c)>0) {
                return true;
            }
        }
        catch (ParseException e)
        {
            return false;
        }
        return false;
    }
}
