package com.andrerichards.andre.weather;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrerichards.andre.weather.com.andrerichards.andre.weather.GetWeatherObjects;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;

/**
 * Created by Andre on 12/24/2015.
 */
public class CitySearch extends Fragment {
    @Bind(R.id.cityEntry)
    EditText cityEntry;
    @Bind(R.id.background)
    ImageView background;
    @Bind(R.id.cityField)
    TextView cityName;
    @Bind(R.id.temperature)
    TextView temperature;
    @Bind(R.id.weatherIcon)
    ImageView weatherIcon;
    @Bind(R.id.weatherCondition)
    TextView condition;
    @Bind(R.id.date)
    TextView dateDisplay;
    @Bind(R.id.maxTemp)
    TextView maxTemp;
    @Bind(R.id.minTemp)
    TextView minTemp;
    @Bind(R.id.sunrise)
    TextView sunrise;
    @Bind(R.id.sunset)
    TextView sunset;
    @Bind(R.id.windSpd)
    TextView windSpd;
    @Bind(R.id.weatherFrame)
    FrameLayout weatherFrame;

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String API_KEY = "&units=imperial&appid=ef4fe2ec9c96d75eb824cf8e9b2cf61a";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, layout);
        showDateDisplay();
        showGeoWeather(layout);
        return layout;
    }

    public void showGeoWeather(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Criteria criteria = new Criteria();
        String provider = lm.getBestProvider(criteria, false);
        if(provider!=null && !provider.equals("")){
//            Location location = lm.getLastKnownLocation(provider);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            getGeoWeather(latitude, longitude);
        } else Toast.makeText(getActivity(), "Location could not be found", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.weatherButton)
    public void getWeatherClick(View v) {
        String city = cityEntry.getText().toString();
        showWeather(city);
    }

    public void showWeather(String city){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);

        retrofit.Retrofit retrofit = new retrofit.Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        WeatherServiceAPI service = retrofit.create(WeatherServiceAPI.class);
        Call<GetWeatherObjects> weatherObjects = service.JSONObjects(WEATHER_URL+"q="+city+API_KEY);
        weatherObjects.enqueue(new Callback<GetWeatherObjects>() {
            @Override
            public void onResponse(Response<GetWeatherObjects> response, retrofit.Retrofit retrofit) {
                cityName.setText(response.body().getName() + ", " + response.body().getSys().getCountry());
                cityName.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                        android.R.anim.fade_in));
                background.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                        android.R.anim.fade_in));

                int temp = (int) Math.round(response.body().getMain().getTemp());
                temperature.setText((temp) + " \u2109");
                String cond = response.body().getWeather().get(0).getDescription();
                condition.setText(Character.toUpperCase(cond.charAt(0)) + cond.substring(1));
                int mxTmp = (int) Math.round(response.body().getMain().getTemp_max());
                maxTemp.setText("Max Temp: " + (mxTmp) + " \u2109");
                int mnTmp = (int) Math.round(response.body().getMain().getTemp_min());
                minTemp.setText("Min Temp: " + (mnTmp) + " \u2109");
                windSpd.setText("Wind Speed: " + response.body().getWind().getSpeed() + "mph");

                long sRise = response.body().getSys().getSunrise();
                sunrise.setText("Sunrise: " + convertEpoch(sRise));
                long sSet = response.body().getSys().getSunset();
                sunset.setText("Sunset: " + convertEpoch(sSet));

                String iconID = response.body().getWeather().get(0).getIcon();
                setWeatherIcon(iconID);
                weatherFrame.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("flow", "failure" + t.getMessage());
            }
        });
    }

    public void getGeoWeather(double lat, double lon){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);

        retrofit.Retrofit retrofit = new retrofit.Retrofit.Builder()
                .baseUrl(WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        WeatherServiceAPI service = retrofit.create(WeatherServiceAPI.class);
        Call<GetWeatherObjects> weatherObjects = service.JSONObjects(WEATHER_URL+"lat="+30+"&lon="+145+API_KEY);
        weatherObjects.enqueue(new Callback<GetWeatherObjects>() {
            @Override
            public void onResponse(Response<GetWeatherObjects> response, retrofit.Retrofit retrofit) {
                cityName.setText(response.body().getName() + ", " + response.body().getSys().getCountry());
                cityName.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                        android.R.anim.fade_in));
                background.startAnimation(AnimationUtils.loadAnimation(getActivity().getBaseContext(),
                        android.R.anim.fade_in));

                int temp = (int) Math.round(response.body().getMain().getTemp());
                temperature.setText((temp) + " \u2109");
                String cond = response.body().getWeather().get(0).getDescription();
                condition.setText(Character.toUpperCase(cond.charAt(0)) + cond.substring(1));
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
                setWeatherIcon(iconID);
                weatherFrame.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("flow", "failure" + t.getMessage());
            }
        });
    }

    public void setWeatherIcon(String iconID) {
        switch (iconID) {
            case "01d":
                weatherIcon.setImageResource(R.drawable.sun_icon);
                background.setBackgroundResource(R.drawable.sunny);
                break;
            case "02d":
            case "04d":
                weatherIcon.setImageResource(R.drawable.partcloudy_icon);
                background.setBackgroundResource(R.drawable.cloudy);
                break;
            case "03d":
                weatherIcon.setImageResource(R.drawable.cloudy_icon);
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
            case "04n":
                weatherIcon.setImageResource(R.drawable.night_icon);
                background.setBackgroundResource(R.drawable.nightcloudy);
                break;
            case "03n":
                weatherIcon.setImageResource(R.drawable.cloudy_icon);
                background.setBackgroundResource(R.drawable.nightcloudy);
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
