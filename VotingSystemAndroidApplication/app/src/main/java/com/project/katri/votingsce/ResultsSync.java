package com.project.katri.votingsce;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Katri on 10/02/2018.
 */

public class ResultsSync extends AsyncTask<String, Void, String> {
    public AsyncResultsResponse delegate = null;
    public Activity activity;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> resultList;
    public TextView data;
    String Url = "";
    String ExistingVote;

    public ResultsSync(Activity activity, String ExistingVote) {
        this.activity = activity;
        resultList = new ArrayList<>();
        this.ExistingVote = ExistingVote;

        Url = "https://morning-anchorage-32230.herokuapp.com/sceresultse";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        String VN = "'" + ExistingVote + "'";

        try {
            URL url = new URL(Url);
            JSONObject encryptedDataParams = new JSONObject();
            encryptedDataParams.put("VoteNum", VN);

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
        if (pDialog.isShowing()) pDialog.dismiss();

        if(!result.equals("")){
            try {
                JSONObject response  = new JSONObject(result);
                JSONArray UserVotings = new JSONArray(AES.decrypt(response.getString("Str"),null));
                //JSONArray UserVotings = new JSONArray(result);
                for (int i = 0; i < UserVotings.length(); i++) {
                    JSONObject c = UserVotings.getJSONObject(i);

                    String name = c.getString("Name");
                    String amount = c.getString("Amount");

                    HashMap<String, String> contact = new HashMap<>();
                    contact.put("Name", name);
                    contact.put("Amount", amount);

                    resultList.add(contact);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(activity.getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
        }

        delegate.processResult(resultList,0);
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
