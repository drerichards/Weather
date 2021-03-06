package com.andrerichards.andre.weather;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Andre on 12/24/2015.
 */
public class CitySearch extends Fragment {

    private RelativeLayout layout;
    protected EditText cityEntry;
    protected Button weatherBtn;
    private CityListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.search_fragment, container, false);
        cityEntry = (EditText) layout.findViewById(R.id.cityEntry);

        weatherBtn = (Button) layout.findViewById(R.id.weatherButton);
        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherDisplay weather = new WeatherDisplay();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.parent_fragment, weather);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return layout;
    }

    public interface CityListener {
        public void cityListener(String city);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CityListener){
            listener = (CityListener) context;
        } else {
            throw new ClassCastException(context.toString()+"CitySearch.CityListener needed");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void addCity(String uri){
        String newTime = String.valueOf(System.currentTimeMillis());
        listener.cityListener(newTime);
    }
}
