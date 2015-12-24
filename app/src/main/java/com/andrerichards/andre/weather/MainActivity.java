package com.andrerichards.andre.weather;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String[] cityArray;
    private Button searchButton;
    private String searchUrl = "http://api.openweathermap.org/data/2.5/weather?q=";
    private String apiKey = "&APPID=ef4fe2ec9c96d75eb824cf8e9b2cf61a";
    private String query;
    private ListView cityList;
    private EditText cityEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cityEntry = (EditText) findViewById(R.id.cityEntry);
        cityList = (ListView) findViewById(R.id.cityListView);
        searchButton = (Button) findViewById(R.id.searchButton);

        

    }

    private class JsonSearchTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                parseResult(sendQuery(query));
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        private String sendQuery(String query) throws IOException
        {
            String queryResult = "";

            URL searchURL = new URL(query);

            HttpURLConnection httpURLConnection = (HttpURLConnection) searchURL.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);

                String line = null;

                while ((line = bufferedReader.readLine()) != null)
                {
                    queryResult += line;
                }

                bufferedReader.close();
            }

            return queryResult;
        }

        private void parseResult(String json) throws JSONException
        {
            String parsedResult = "";

            JSONObject jsonObject = new JSONObject(json);

            JSONObject jsonObject_responseData = jsonObject.getJSONObject("responseData");

            JSONArray jsonArray_results = jsonObject_responseData.getJSONArray("results");

            cityArray = new String[jsonArray_results.length()];

            for(int i = 0; i < jsonArray_results.length(); i++)
            {
                JSONObject jsonObject_i = jsonArray_results.getJSONObject(i);
                parsedResult = "title: " + jsonObject_i.getString("title") + "\n";
                parsedResult += "url: " + jsonObject_i.getString("url") + "\n";
                //parsedResult += "\n";
                cityArray[i] = parsedResult;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
