package com.company.Weather;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class WeatherAPI {

    //Generates the URI for the request based on a city (user's input)
    public static String getWeatherAPIRequest(String city) {
        String APIkey = "0efccd5a0c1d61b091b4c75fbc5b050a";
        String request = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APIkey;

        return request;
    }

    //Generates the URI for the request based on the ISS location
    public static String getWeatherAPIRequest(double lat, double lon) {
        String APIkey = "0efccd5a0c1d61b091b4c75fbc5b050a";
        String request = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + APIkey;

        return request;
    }

    //Calls the API and handles client & server errors
    public static WeatherResponse getWeather(String request) {

        WebClient client = WebClient.create(request);

        WeatherResponse weatherjsonresponse = null;

        try {
            Mono<WeatherResponse> response = client
                    .get()
                    .retrieve()
                    .bodyToMono(WeatherResponse.class);

            weatherjsonresponse = response.share().block();

        }
        catch (WebClientResponseException we) {

            int statusCode = we.getRawStatusCode();
            if (statusCode >= 400 && statusCode <500){
                System.out.println("Client Error");
            }
            else if (statusCode >= 500 && statusCode <600){
                System.out.println("Server Error");
            }
            System.out.println("Message: " + we.getMessage());
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        return weatherjsonresponse;
    }

    //Displays weather information. If the response is null: returns string stating the location does not exist.
    public static void displayWeather(WeatherResponse weatherjsonresponse){
        if (weatherjsonresponse == null){
            System.out.println("This location does not exist, please try again.");
        }

        //If name = null: then the lat and long do not correspond to a city, so return string and
        // weather at the closest station
        else if (weatherjsonresponse.name == ""){
            System.out.println("The ISS is not above any cities");
            System.out.println("Weather at the closest station: " + weatherjsonresponse.weather[0].description);
            WeatherTempInfo main = weatherjsonresponse.main;

            //Convert Kelvin into Fahrenheit and Celsius
            double feelTempF = kelvinToFahrenheit(main.feels_like);
            double feelTempC = kelvinToCelsius(main.feels_like);
            double tempF = kelvinToFahrenheit(main.temp);
            double tempC = kelvinToCelsius(main.temp);

            //Display all weather info
            System.out.format("Current temperature: %.0f in Fahrenheit / %.1f in Celsius \n", tempF, tempC);
            System.out.format("Feels like: %.0f in Fahrenheit / %.1f in Celsius \n", feelTempF, feelTempC);
            System.out.println("Current humidity: " + main.humidity + "%");
            System.out.format("Min temperature today: %.0f in Fahrenheit / %.1f in Celsius. \n",
                    kelvinToFahrenheit(main.temp_min), kelvinToCelsius(main.temp_min));
            System.out.format("Max temperature today: %.0f in Fahrenheit / %.1f in Celsius. \n",
                    kelvinToFahrenheit(main.temp_max), kelvinToCelsius(main.temp_max));
            WeatherWindInfo wind = weatherjsonresponse.wind;
            System.out.format("Wind speed: %.2f m/sec\n", wind.speed);
            String.format("%n");
        }

        //If the city exists (returns not null), then display city and country info + weather info
        else {
            //Displays city + country
            System.out.println("City: " + weatherjsonresponse.name);
            WeatherSystemInfo sys = weatherjsonresponse.sys;
            System.out.println("Country code: " + sys.country);

            //Displays weather description
            System.out.println("Weather: " + weatherjsonresponse.weather[0].description);
            WeatherTempInfo main = weatherjsonresponse.main;

            //Convert Kelvin into Fahrenheit and Celsius
            double feelTempF = kelvinToFahrenheit(main.feels_like);
            double feelTempC = kelvinToCelsius(main.feels_like);
            double tempF = kelvinToFahrenheit(main.temp);
            double tempC = kelvinToCelsius(main.temp);

            //Display all weather info
            System.out.format("Current temperature: %.0f in Fahrenheit / %.1f in Celsius \n", tempF, tempC);
            System.out.format("Feels like: %.0f in Fahrenheit / %.1f in Celsius \n", feelTempF, feelTempC);
            System.out.println("Current humidity: " + main.humidity + "%");
            System.out.format("Min temperature today: %.0f in Fahrenheit / %.1f in Celsius. \n",
                    kelvinToFahrenheit(main.temp_min), kelvinToCelsius(main.temp_min));
            System.out.format("Max temperature today: %.0f in Fahrenheit / %.1f in Celsius. \n",
                    kelvinToFahrenheit(main.temp_max), kelvinToCelsius(main.temp_max));
            WeatherWindInfo wind = weatherjsonresponse.wind;
            System.out.format("Wind speed: %.2f m/sec\n", wind.speed);
            String.format("%n");
        }

    }

    //Converts a response from API with a lat/long into location info
    public static void displayLocation(WeatherResponse weatherjsonresponse){
        if (weatherjsonresponse == null){
            System.out.println("This location does not exist, please try again.");
        }

        //If no city name, then ISS is not above any city. Display only lat/long.
        else if (weatherjsonresponse.name == ""){
            System.out.println("The ISS is not above any cities");
            WeatherCoordinates coord = weatherjsonresponse.coord;
            System.out.println("Latitude: " + coord.lat);
            System.out.println("Longitude: " + coord.lon);
            String.format("%n");
        }

        //If city name, then display city and country info.
        else {
            System.out.println("City: " + weatherjsonresponse.name);
            WeatherSystemInfo countryCode = weatherjsonresponse.sys;
            System.out.println("Country code: " + countryCode.country);
            WeatherCoordinates coord = weatherjsonresponse.coord;
            System.out.println("Latitude: " + coord.lat);
            System.out.println("Longitude: " + coord.lon);
            String.format("%n");
        }

    }

    //Methods to convert kelvin -> Fahrenheit or Celsius
    private static double kelvinToCelsius(double kelvin){
        return (kelvin-273.5);
    }
    private static double kelvinToFahrenheit(double kelvin){
        return (1.8* (kelvin-273) + 32);
    }

}
