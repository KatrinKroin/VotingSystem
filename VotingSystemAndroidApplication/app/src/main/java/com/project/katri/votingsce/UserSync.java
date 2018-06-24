package com.project.katri.votingsce;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Circle;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Katri on 07/01/2018.
 */

public class UserSync extends AsyncTask<String, Void, String> {
    public Activity activity;
    private EditText UserEmail;
    private EditText Password;
    private ProgressDialog pDialog;

    public UserSync(Activity activity){
        this.activity = activity;
        this.UserEmail = (EditText) activity.findViewById(R.id.Email);
        this.Password = (EditText) activity.findViewById(R.id.Password);
    }

    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity,ProgressDialog.THEME_DEVICE_DEFAULT_DARK);//ProgressDialog.STYLE_SPINNER   R.style.MyDialogTheme
        Circle doubleBounce = new Circle();
        pDialog.setIndeterminateDrawable(doubleBounce);
        pDialog.setCancelable(false);
        pDialog.setMessage(" Loading... ");
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
    }


    protected String doInBackground(String... arg0) {
        String Email = "'" + UserEmail.getText().toString() + "'";
        String PS = "'" + new SHA().sha(Password.getText().toString()) + "'";

        try {
            URL url = new URL("https://morning-anchorage-32230.herokuapp.com/scelogine"); // scelogine

            JSONObject encryptedDataParams = new JSONObject();

            encryptedDataParams.put("Email",  Email);
            encryptedDataParams.put("Password", PS );

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("Str", AES.encrypt(encryptedDataParams.toString(), null));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
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
                CreateUser(result);
                Intent intent = new Intent(activity,VotingList.class);
                intent.putExtra("flag", "0");
                activity.startActivity(intent);
                activity.finish();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), "Server timeout, please try again!", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(activity.getApplicationContext(), "Wrong credentials, please try again!", Toast.LENGTH_LONG).show();
        }
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

    public  void CreateUser(String jsonStr) throws JSONException {
        JSONObject response  = new JSONObject(jsonStr);
        JSONObject JO = new JSONObject(AES.decrypt(response.getString("Str"),null));
       // JSONObject JO  = new JSONObject(jsonStr);
        try{
            User NewUser = User.init(JO.getString("UserID"),JO.getString("Name"),JO.getString("Email"),JO.getString("Password"),Boolean.parseBoolean(JO.getString("Admin")));
        }
        catch (AssertionError error){
            Toast.makeText(activity.getApplicationContext(), "Error: the user already exist!", Toast.LENGTH_LONG).show();
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }
}

