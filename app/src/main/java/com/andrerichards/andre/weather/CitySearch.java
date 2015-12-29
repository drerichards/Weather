package com.andrerichards.andre.weather;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrerichards.andre.weather.com.andrerichards.andre.weather.GetWeatherObjects;
import com.andrerichards.andre.weather.com.andrerichards.andre.weather.Main;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;

/**
 * Created by Andre on 12/24/2015.
 */
public class CitySearch extends Fragment {

    private RelativeLayout layout;
    protected EditText cityEntry;
    protected Button weatherBtn;
    public Button button;

    protected TextView cityName, temperature, condition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.search_fragment, container, false);
        cityEntry = (EditText) layout.findViewById(R.id.cityEntry);
        cityName = (TextView) layout.findViewById(R.id.cityField);
        temperature = (TextView) layout.findViewById(R.id.temperature);
        condition = (TextView) layout.findViewById(R.id.weatherCondition);

        weatherBtn = (Button) layout.findViewById(R.id.weatherButton);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WeatherDisplay weather = new WeatherDisplay();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.add(R.id.parent_fragment, weather);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.d("flow", "clicked");
            }
        });

        button = (Button) layout.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    // set your desired log level
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient httpClient = new OkHttpClient();
                    // add your other interceptors …

                    // add logging as last interceptor
                    httpClient.interceptors().add(logging);  // <-- this is the important line!

                    retrofit.Retrofit retrofit = new retrofit.Retrofit.Builder()
                            .baseUrl("http://api.openweathermap.org/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient)
                            .build();

                    WeatherServiceAPI service = retrofit.create(WeatherServiceAPI.class);

                    Call<GetWeatherObjects> weatherObjects = service.listJSONObjects();

                    weatherObjects.enqueue(new Callback<GetWeatherObjects>() {
                        @Override
                        public void onResponse(Response<GetWeatherObjects> response, retrofit.Retrofit retrofit) {

                            GetWeatherObjects getWeatherObjects = new GetWeatherObjects();
                            Main main = new Main();
                            cityName.setText(String.valueOf(getWeatherObjects.getName()));
                            temperature.setText(String.valueOf(main.getTemp()));
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.d("flow", "failure");
                        }
                    });






            }
        });

        return layout;
    }
}
