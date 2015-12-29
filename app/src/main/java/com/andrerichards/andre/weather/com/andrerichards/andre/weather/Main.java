package com.andrerichards.andre.weather.com.andrerichards.andre.weather;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

/**
 * Created by Andre on 12/27/2015.
 */
@Generated("org.jsonschema2pojo")
public class Main {

    private double temp;
    private int pressure;
    private int humidity;
    private double temp_min;
    private double temp_max;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Main() {
    }

    /**
     *
     * @param humidity
     * @param pressure
     * @param temp_max
     * @param temp_min
     * @param temp
     */
    public Main(double temp, int pressure, int humidity, double temp_min, double temp_max) {
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    /**
     *
     * @return
     * The temp
     */
    public double getTemp() {
        return temp;
    }

    /**
     *
     * @param temp
     * The temp
     */
    public void setTemp(double temp) {
        this.temp = temp;
    }

    public Main withTemp(double temp) {
        this.temp = temp;
        return this;
    }

    /**
     *
     * @return
     * The pressure
     */
    public int getPressure() {
        return pressure;
    }

    /**
     *
     * @param pressure
     * The pressure
     */
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public Main withPressure(int pressure) {
        this.pressure = pressure;
        return this;
    }

    /**
     *
     * @return
     * The humidity
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     * The humidity
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Main withHumidity(int humidity) {
        this.humidity = humidity;
        return this;
    }

    /**
     *
     * @return
     * The temp_min
     */
    public double getTemp_min() {
        return temp_min;
    }

    /**
     *
     * @param temp_min
     * The temp_min
     */
    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public Main withTemp_min(double temp_min) {
        this.temp_min = temp_min;
        return this;
    }

    /**
     *
     * @return
     * The temp_max
     */
    public double getTemp_max() {
        return temp_max;
    }

    /**
     *
     * @param temp_max
     * The temp_max
     */
    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public Main withTemp_max(double temp_max) {
        this.temp_max = temp_max;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Main withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
