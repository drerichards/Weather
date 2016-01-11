package com.andrerichards.andre.weather;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrerichards.andre.weather.com.andrerichards.andre.weather.GetWeatherObjects;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
    protected TextView cityName, temperature, condition, dateDisplay, maxTemp, minTemp,
    sunrise, sunset, windSpd;
    protected ImageView weatherIcon, background;

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String API_KEY = "&units=imperial&appid=2de143494c0b295cca9337e1e96b00e0";

    private String anim = "AnimationUtils.loadAnimation(getActivity().getBaseContext(),android.R.anim.fade_in";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.search_fragment, container, false);
        cityEntry = (EditText) layout.findViewById(R.id.cityEntry);
        background = (ImageView) layout.findViewById(R.id.background);
        cityName = (TextView) layout.findViewById(R.id.cityField);
        temperature = (TextView) layout.findViewById(R.id.temperature);
        weatherIcon = (ImageView) layout.findViewById(R.id.weatherIcon);
        condition = (TextView) layout.findViewById(R.id.weatherCondition);
        dateDisplay = (TextView) layout.findViewById(R.id.date);
        maxTemp = (TextView) layout.findViewById(R.id.maxTemp);
        minTemp = (TextView) layout.findViewById(R.id.minTemp);
        sunrise = (TextView) layout.findViewById(R.id.sunrise);
        sunset = (TextView) layout.findViewById(R.id.sunset);
        windSpd = (TextView) layout.findViewById(R.id.windSpd);

        weatherBtn = (Button) layout.findViewById(R.id.weatherButton);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeather();
            }
        });
        return layout;
    }

    public void showWeather(){
        String city = cityEntry.getText().toString();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.interceptors().add(logging);  // <-- this is the important line!

        retrofit.Retrofit retrofit = new retrofit.Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        WeatherServiceAPI service = retrofit.create(WeatherServiceAPI.class);
        Call<GetWeatherObjects> weatherObjects = service.JSONObjects(WEATHER_URL+city+API_KEY);
        weatherObjects.enqueue(new Callback<GetWeatherObjects>() {
            @Override
            public void onResponse(Response<GetWeatherObjects> response, retrofit.Retrofit retrofit) {
                cityName.setText(response.body().getName());
                cityName.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                        android.R.anim.fade_in));
                background.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                        android.R.anim.fade_in));

                int temp = (int) Math.round(response.body().getMain().getTemp());
                temperature.setText((temp) + " \u2109");
                String cond = response.body().getWeather().get(0).getDescription();
                condition.setText(Character.toUpperCase(cond.charAt(0)) + cond.substring(1));
                showDateDisplay();
                int mxTmp = (int) Math.round(response.body().getMain().getTemp_max());
                maxTemp.setText("Max Temp: " + (mxTmp) + " \u2109");
                int mnTmp = (int) Math.round(response.body().getMain().getTemp_min());
                minTemp.setText("Min Temp: " + (mnTmp) + " \u2109");
                windSpd.setText("Wind Speed: " + response.body().getWind().getSpeed() + "mph");

                long sRise = response.body().getSys().getSunrise();
                sunrise.setText("Sunrise: "+convertEpoch(sRise));
                long sSet = response.body().getSys().getSunset();
                sunset.setText("Sunset: "+convertEpoch(sSet));

                String iconID = response.body().getWeather().get(0).getIcon();
                switch (iconID) {
                    case "01d":
                        weatherIcon.setImageResource(R.drawable.sun_icon);
                        background.setBackgroundResource(R.drawable.sunny);
                        break;
                    case "02d":
                        weatherIcon.setImageResource(R.drawable.partcloudy_icon);
                        background.setBackgroundResource(R.drawable.cloudy);
                        break;
                    case "03d":
                        weatherIcon.setImageResource(R.drawable.cloudy_icon);
                        background.setBackgroundResource(R.drawable.cloudy);
                        break;
                    case "04d":
                        weatherIcon.setImageResource(R.drawable.twocloud_icon);
                        background.setBackgroundResource(R.drawable.cloudy);
                        break;
                    case "09d":
                    case "10d":
                        weatherIcon.setImageResource(R.drawable.rain_icon);
                        background.setBackgroundResource(R.drawable.dayrainy);
                        break;
                    case "11d":
                        weatherIcon.setImageResource(R.drawable.bolt_icon);
                        background.setBackgroundResource(R.drawable.bolty);
                        break;
                    case "13d":
                        weatherIcon.setImageResource(R.drawable.snow_icon);
                        background.setBackgroundResource(R.drawable.snowy);
                        break;
                    case "50d":
                        weatherIcon.setImageResource(R.drawable.partcloudy_icon);
                        background.setBackgroundResource(R.drawable.foggy);
                        break;
                    case "01n":
                        weatherIcon.setImageResource(R.drawable.moon_icon);
                        background.setBackgroundResource(R.drawable.starry);
                        break;
                    case "02n":
                        weatherIcon.setImageResource(R.drawable.night_icon);
                        background.setBackgroundResource(R.drawable.nightcloudy);
                        break;
                    case "03n":
                        weatherIcon.setImageResource(R.drawable.cloudy_icon);
                        background.setBackgroundResource(R.drawable.nightcloudy);
                        break;
                    case "04n":
                        weatherIcon.setImageResource(R.drawable.twocloud_icon);
                        background.setBackgroundResource(R.drawable.nightcloudy);
                        break;
                    case "09n":
                    case "10n":
                        weatherIcon.setImageResource(R.drawable.rain_icon);
                        background.setBackgroundResource(R.drawable.rainy);
                        break;
                    case "11n":
                        weatherIcon.setImageResource(R.drawable.bolt_icon);
                        background.setBackgroundResource(R.drawable.bolty);
                        break;
                    case "13n":
                        weatherIcon.setImageResource(R.drawable.snow_icon);
                        background.setBackgroundResource(R.drawable.snowy);
                        break;
                    case "50n":
                        weatherIcon.setImageResource(R.drawable.partcloudy_icon);
                        background.setBackgroundResource(R.drawable.nightfoggy);
                        break;
                    default:
                        weatherIcon.setImageResource(R.drawable.cloudy_icon);
                        background.setBackgroundResource(R.drawable.cloudy);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("flow", "failure" + t.getMessage());
            }
        });
    }

    public String convertEpoch(long sunTime){
        Date date = new Date(sunTime*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String sunTimeConverted = sdf.format(date);
        return sunTimeConverted;
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
