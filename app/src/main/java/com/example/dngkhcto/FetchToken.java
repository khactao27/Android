package com.example.dngkhcto;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;

public class FetchToken extends AsyncTask<String, Void, String> {
    private WeakReference<TextView> timeLocal;
    private WeakReference<TextView> timeServer;
    private WeakReference<TextView> token;
    private LocalTime timeClick;

    public FetchToken(TextView timeLocal, TextView timeServer, TextView token, LocalTime time) {
        this.timeLocal = new WeakReference<>(timeLocal);
        this.timeServer = new WeakReference<>(timeServer);
        this.token = new WeakReference<>(token);
        this.timeClick = time;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... strings) {
        String postData = strings[0];
        String urlString = "http://203.171.20.94:8083/weatherforecast/GetToken";
        try{
            // Data;
            //String jsonInputString = "{\"name\": \"Upendra\", \"job\": \"Programmer\"}";
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");//important
            httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.connect();

            //write data to the server using BufferedWriter
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            Log.d("data", postData);
            writer.write(postData);
            writer.flush();

            //get Response code and check if valid (HTTP OK)
            int responseCode = httpURLConnection.getResponseCode();
            Log.d("status", " "+responseCode);
            if(responseCode == 200){//if valid, read result from server
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while (((line = reader.readLine()) != null)){
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            // convert the response into a JSON object
            JSONObject jsonToken = new JSONObject(s);
            Log.d("response", jsonToken.toString());
            String studentId = jsonToken.getString("studentId");
            String studentToken = jsonToken.getString("studentToken");
            String serverTime = jsonToken.getString("serverTime");
                timeLocal.get().setText((LocalTime.now()).toString());
                timeServer.get().setText(serverTime);
                token.get().setText(studentToken);

        }catch (Exception e){
            e.printStackTrace();
            timeLocal.get().setText(timeClick.toString());
            timeServer.get().setText("No response");
            token.get().setText("No token");
        }
    }
}
