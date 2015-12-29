package com.andrerichards.andre.weather;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Andre on 12/24/2015.
 */
public class WeatherDisplay extends Fragment {

    private RelativeLayout layout;
    protected TextView cityName, temperature, condition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.weather_fragment, container, false);

        cityName = (TextView) layout.findViewById(R.id.cityField);
        temperature = (TextView) layout.findViewById(R.id.temperature);
        condition = (TextView) layout.findViewById(R.id.weatherCondition);

        return layout;
    }



}
