package com.project.katri.votingsce;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import com.github.ybq.android.spinkit.style.Circle;
import com.project.katri.votingsce.BuildConfig;

/**
 * Created by Katri on 10/02/2018.
 */

public class SendBalotSync extends AsyncTask<String, Void, String> implements ActivityCompat.OnRequestPermissionsResultCallback{
    private ProgressDialog pDialog;
    public Context context;
    int flag;
    User CurrentUser;
    String ExistingVote;
    String CandidateID;


    public SendBalotSync(Context context, int flag, String ExistingVote, String CandidateID) {
        this.context = context;
        this.flag = flag;
        CurrentUser = CurrentUser.getInstance();
        this.ExistingVote = ExistingVote;
        this.CandidateID = CandidateID;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        Circle doubleBounce = new Circle();
        pDialog.setIndeterminateDrawable(doubleBounce);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }

    protected String doInBackground(String... arg0) {
        //Log.i("key------------------",obj.BuildKey());
        String ID = "'" + CurrentUser.GetUserID() + "'";
        String VN = "'" + ExistingVote + "'";
        String CID = "'" + CandidateID + "'";
        String answer = "false";

        //if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                URL url = new URL("https://morning-anchorage-32230.herokuapp.com/sceuservotede");

                JSONObject encryptedDataParams = new JSONObject();

                encryptedDataParams.put("UserID", ID);
                encryptedDataParams.put("VoteNum", VN);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Str", AES.encrypt(encryptedDataParams.toString(), null));

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
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
                    answer = sb.toString();
                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

            try {
                Log.i("---",answer);
                JSONObject response  = new JSONObject(answer);
                String JO = AES.decrypt(response.getString("Str"),null);
                Log.i("---",JO);
                if (JO.equals("true")) {
                    try {
                        URL url = new URL("https://morning-anchorage-32230.herokuapp.com/scesendballote");

                        BallotKeyBuilder Key = new BallotKeyBuilder(context.getApplicationContext());
                        // TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        //String key = "1.0.8 User --- "+ CurrentUser.GetUserID()+"--- Manufacturer --- " + Build.MANUFACTURER + " --- Model --- " + Build.MODEL + " --- Android Version --- " + Build.VERSION.SDK_INT + " --- IMEI --- " + tm.getDeviceId() + " --- Time --- " + (new Date()).toString();

                        JSONObject encryptedDataParams = new JSONObject();

                        encryptedDataParams.put("CandidateID", CID);
                        encryptedDataParams.put("VoteNum", VN);
                        encryptedDataParams.put("VoteKey", "'" + Key.BuildKey() + "'"); //    key

                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("Str", AES.encrypt(encryptedDataParams.toString(), null));

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(15000 /* milliseconds */);
                        conn.setConnectTimeout(15000 /* milliseconds */);
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

                           // Log.i("Yeahhhh2222222", sb.toString());

                            return sb.toString();
                        } else {
                            return new String("false : " + responseCode);
                        }
                    } catch (Exception e) {
                        return new String("Exception: " + e.getMessage());
                    }
                } else return answer;
            } catch (JSONException e) {
               return "false";
            }

        //}
        //else{

           // Log.i("-------------   ",ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) + "   "+ PackageManager.PERMISSION_GRANTED + "   " + "NoPermission");
        //    return "NoPermission";
        //}
    }

    @Override
    protected void onPostExecute(String answer) {
        //if(result.equals("")){
        //    Toast.makeText(context.getApplicationContext(), "Error in server result!", Toast.LENGTH_LONG).show();
        //}

        String result = null;

        try {
            Log.i("---",answer);
            JSONObject response = new JSONObject(answer);
            result = AES.decrypt(response.getString("Str"),null);
            Log.i("---",result);
        } catch (JSONException e) {
            result = "false";
            Log.i("---",result);
        }

        if (pDialog.isShowing()) pDialog.dismiss();

        //if(result.equals("NoPermission")){
        //    ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.READ_PHONE_STATE},1 );
        //}
        //else{
            Intent intent = new Intent(context, SuccessSend.class);
            intent.putExtra("result", result);
            context.startActivity(intent);
        //}
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

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        if( RC == 1){
            SendBalotSync balot = new SendBalotSync(context,flag ,this.ExistingVote ,this.CandidateID);
            balot.execute();
        }
        else {
            Toast.makeText((Activity) context,"Permission Canceled, Now your application cannot access PHONE STATE.", Toast.LENGTH_LONG).show();
        }
    }
}
