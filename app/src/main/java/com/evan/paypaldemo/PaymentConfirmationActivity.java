package com.evan.paypaldemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class PaymentConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        //Getting Intent
        Intent intent = getIntent();


        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {

        Log.v("wtf", jsonDetails + "payment response");
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus = (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount + " CAD");

        new DownloadLink().execute();

    }

    private class DownloadLink extends AsyncTask<Void, Void, Void > {


        @Override
        protected Void doInBackground(Void ... params) {
            // TODO Auto-generated method stub

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/oauth2/token");

            try {
                String text = "<client id>" + ":" + "<client secret id>";
                byte[] data = text.getBytes("UTF-8");
                String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

                httppost.addHeader("content-type", "application/x-www-form-urlencoded");
                httppost.addHeader("Authorization", "Basic " + base64);

                StringEntity se = new StringEntity("grant_type=client_credentials");
                httppost.setEntity(se);

// Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String responseContent = EntityUtils.toString(response.getEntity());
                Log.d("Response", responseContent);
            } catch (ClientProtocolException e) {
// TODO Auto-generated catch block
            } catch (IOException e) {
// TODO Auto-generated catch block
            }
            return null;

        }
    }

}