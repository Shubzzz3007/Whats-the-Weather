package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText input;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.editText);
        result = (TextView) findViewById(R.id.resultTextView);

    }

    public void getWeather(View view)
    {
        DownloadWeather task = new DownloadWeather();
        try {
            task.execute("https://openweathermap.org/data/2.5/weather?q="+input.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DownloadWeather extends AsyncTask<String,Void,String>
    {

       String Json = "";
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data!=-1)
                {
                    char current = (char) data;
                    Json += current;
                    data = reader.read();
                }
                return Json;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String message = "";
            try {
                JSONObject object = new JSONObject(s);
                String weatherInfo = object.getString("weather");
                JSONObject otherInfo = object.getJSONObject("main");
                JSONArray arr = new JSONArray(weatherInfo);

                String temp = otherInfo.getString("temp");
                String feel = otherInfo.getString("feels_like");
                String minTemp = otherInfo.getString("temp_min");
                String maxTemp = otherInfo.getString("temp_max");
                String pressure = otherInfo.getString("pressure");
                String humidity = otherInfo.getString("humidity");

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject part = arr.getJSONObject(i);
                    String main = part.getString("main");
                    String description = part.getString("description");

                    if(!main.equals("") && !description.equals(""))
                    {
                        message += main + " : " + description + "\r\n";
                        message += "Temperature" + " : " + temp + "\r\n";
                        message += "Feel's Like" + " : " + feel + "\r\n";
                        message += "Minimum Temperature" + " : " + minTemp + "\r\n";
                        message += "Maximum Temperature" + " : " + maxTemp + "\r\n";
                        message += "Pressure" + " : " + pressure + "\r\n";
                        message += "Humidity" + " : " + humidity + "\r\n";



                    }
                }
                if(!message.equals(""))
                {
                    result.setText(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}