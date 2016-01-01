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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.andrerichards.andre.weather.com.andrerichards.andre.weather.GetWeatherObjects;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;


/**
 * Created by Andre on 12/24/2015.
 */
public class WeatherDisplay extends Fragment {

    private RelativeLayout layout;
    private TextView cityName, temperature, condition, backButton, dateDisplay;
    private ImageView weatherIcon, background;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.weather_fragment, container, false);

        background = (ImageView) layout.findViewById(R.id.background);
        cityName = (TextView) layout.findViewById(R.id.cityField);
        temperature = (TextView) layout.findViewById(R.id.temperature);
        weatherIcon = (ImageView) layout.findViewById(R.id.weatherIcon);
        condition = (TextView) layout.findViewById(R.id.weatherCondition);
        dateDisplay = (TextView) layout.findViewById(R.id.date);
        showDateDisplay();
        showWeather();


        backButton = (Button) layout.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CitySearch search = new CitySearch();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.parent_fragment, search);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return layout;
    }

    public void showWeather(){
        CitySearch search = new CitySearch();
//        String inputCity = search.cityEntry.getText().toString();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
                    // add your other interceptors …

                    // add logging as last interceptor
        httpClient.interceptors().add(logging);  // <-- this is the important line!

        retrofit.Retrofit retrofit = new retrofit.Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
//                .client(httpClient)
                .build();

        WeatherServiceAPI service = retrofit.create(WeatherServiceAPI.class);
        Call<GetWeatherObjects> weatherObjects = service.listJSONObjects();
        weatherObjects.enqueue(new Callback<GetWeatherObjects>() {
            @Override
            public void onResponse(Response<GetWeatherObjects> response, retrofit.Retrofit retrofit) {
                cityName.setText(response.body().getName());
                int temp = (int) Math.round(response.body().getMain().getTemp() - 220.57);
                temperature.setText((temp) + " \u2109");
                String cond = response.body().getWeather().get(0).getDescription();
                condition.setText(Character.toUpperCase(cond.charAt(0)) + cond.substring(1));
                String iconID = response.body().getWeather().get(0).getIcon();

//                switch (iconID){
//                    case "01d" :
//                        weatherIcon.setImageResource(R.drawable.sun_icon);
//                        background.setBackgroundResource(R.drawable.sunny);
//                        break;
//                    case "02d" :
//                        weatherIcon.setImageResource(R.drawable.partcloudy_icon);
//                        background.setBackgroundResource(R.drawable.starry);
//                        break;
//                    case "03d" :
//                        weatherIcon.setImageResource(R.drawable.cloudy_icon);
//                        background.setBackgroundResource(R.drawable.starry);
//                        break;
//                    case "04d" :
//                        weatherIcon.setImageResource(R.drawable.twocloud_icon);
//                        background.setBackgroundResource(R.drawable.starry);
//                        break;
//                    case "09d" :
//                        weatherIcon.setImageResource(R.drawable.rain_icon);
//                        background.setBackgroundResource(R.drawable.rainy);
//                        break;
//                    case "10d" :
//                        weatherIcon.setImageResource(R.drawable.rain_icon);
//                        background.setBackgroundResource(R.drawable.rainy);
//                        break;
//                    case "11d" :
//                        weatherIcon.setImageResource(R.drawable.bolt_icon);
//                        background.setBackgroundResource(R.drawable.bolty);
//                        break;
//                    case "13d" :
//                        weatherIcon.setImageResource(R.drawable.snow_icon);
//                        background.setBackgroundResource(R.drawable.snowy);
//                        break;
//                    case "50d" :
//                        weatherIcon.setImageResource(R.drawable.partcloudy_icon);
//                        background.setBackgroundResource(R.drawable.foggy);
//                        break;
//                    case "01n" :
//                        weatherIcon.setImageResource(R.drawable.moon_icon);
//                        background.setBackgroundResource(R.drawable.starry);
//                        break;
//                    case "02n" :
//                        weatherIcon.setImageResource(R.drawable.night_icon);
//                        background.setBackgroundResource(R.drawable.nightcloudy);
//                        break;
//                    case "03n" :
//                        weatherIcon.setImageResource(R.drawable.cloudy_icon);
//                        background.setBackgroundResource(R.drawable.nightfoggy);
//                        break;
//                    case "04n" :
//                        weatherIcon.setImageResource(R.drawable.twocloud_icon);
//                        background.setBackgroundResource(R.drawable.nightcloudy);
//                        break;
//                    case "09n" :
//                        weatherIcon.setImageResource(R.drawable.rain_icon);
//                        background.setBackgroundResource(R.drawable.rainy);
//                        break;
//                    case "10n" :
//                        weatherIcon.setImageResource(R.drawable.rain_icon);
//                        background.setBackgroundResource(R.drawable.rainy);
//                        break;
//                    case "11n" :
//                        weatherIcon.setImageResource(R.drawable.bolt_icon);
//                        background.setBackgroundResource(R.drawable.bolty);
//                        break;
//                    case "13n" :
//                        weatherIcon.setImageResource(R.drawable.snow_icon);
//                        background.setBackgroundResource(R.drawable.snowy);
//                        break;
//                    case "50n" :
//                        weatherIcon.setImageResource(R.drawable.partcloudy_icon);
//                        background.setBackgroundResource(R.drawable.nightfoggy);
//                        break;
//                    default:
//                        weatherIcon.setImageResource(R.drawable.cloudy_icon);
//                        background.setBackgroundResource(R.drawable.cloudy);
//                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("flow", "failure");
            }
        });
    }

    private void showDateDisplay() {
        try {
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("E MMMM dd, yyyy");
            String dateString = dateFormat.format(currentDate);
            dateDisplay.setText(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
