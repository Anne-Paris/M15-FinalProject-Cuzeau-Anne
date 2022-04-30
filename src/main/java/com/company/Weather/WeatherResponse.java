package com.company.Weather;

public class WeatherResponse {
    public String name;
    public WeatherCoordinates coord;
    public WeatherTempInfo main;
    public WeatherConditions[] weather;
    public String base;
    public int visibility;
    public int cod;
    public String message;
    public WeatherSystemInfo sys;
    public int timezone;
    public int id;
    public int dt;
    public WeatherWindInfo wind;
    public WeatherCloudsInfo clouds;
}
