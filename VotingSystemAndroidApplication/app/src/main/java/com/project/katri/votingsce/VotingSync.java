package com.project.katri.votingsce;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Katri on 03/02/2018.
 */

public class VotingSync extends AsyncTask<String, Void, String> {
    public AsyncResponse delegate = null;
    public Activity activity;
    private ProgressDialog pDialog;
    private User CurrentUser;
    private ArrayList<Voting> UserVotings;
    public TextView data;
    private String Url = "";
    private int flag;

    public VotingSync(Activity activity, int flag) {
        this.flag = flag;
        this.activity = activity;
        UserVotings = new ArrayList<>();
        CurrentUser = CurrentUser.getInstance();

        if(flag == 1 && CurrentUser.GerStatus() == true)
            Url = "https://morning-anchorage-32230.herokuapp.com/scegetallvotese";
        else
            Url = "https://morning-anchorage-32230.herokuapp.com/scegetvotese";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        lockScreenOrientation();
        pDialog = new ProgressDialog(activity,ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        Circle doubleBounce = new Circle();
        pDialog.setIndeterminateDrawable(doubleBounce);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        String ID = "'" + CurrentUser.GetUserID() + "'";

        try {
            URL url = new URL(Url);

            JSONObject encryptedDataParams = new JSONObject();
            encryptedDataParams.put("UserID", ID);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("Str", AES.encrypt(encryptedDataParams.toString(), null));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 );
            conn.setConnectTimeout(15000 );
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            } else {
                return new String("False : " + responseCode);
            }
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("")){
            //Log.i("Server Result!---",result);
            try {
                JSONObject response  = new JSONObject(result);
                JSONArray UserVotings = new JSONArray(AES.decrypt(response.getString("Str"),null));
                //JSONArray UserVotings = new JSONArray(result);
                for (int i = 0; i < UserVotings.length(); i++) {
                    JSONObject c = UserVotings.getJSONObject(i);

                    if(flag == 0 && Voting.Compare(c.getString("Start"), c.getString("Finish")) == false)
                        continue;
                    try {
                        this.UserVotings.add(new Voting(Integer.parseInt(c.getString("VoteNum")),c.getString("VoteName"),c.getString("VoteDescription"),c.getString("Start"),c.getString("Finish")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(activity.getApplicationContext(),"Parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(),"Server's timeout!",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(activity.getApplicationContext(),"Our servers currently have an issue, please try again later!",Toast.LENGTH_LONG).show();
        }

        delegate.processFinish(this.UserVotings,flag);
        if (pDialog != null  && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        unlockScreenOrientation();
    }

    private void lockScreenOrientation() {
        int currentOrientation = activity.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private void unlockScreenOrientation() {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}



