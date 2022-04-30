package com.company.Space;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class SpaceAPI {
    private static double latitude;
    private static double longitude;

    public static double getISSlongitude() {
        getISSLatLong();
        return longitude;
    }

    public static double getISSlatitude() {
        getISSLatLong();
        return latitude;
    }

    private static void getISSLatLong() {

        WebClient client = WebClient.create("http://api.open-notify.org/iss-now.json");

        SpaceResponse spaceResponse = null;

        try {
            Mono<SpaceResponse> response = client
                    .get()
                    .retrieve()
                    .bodyToMono(SpaceResponse.class);

            spaceResponse = response.share().block();
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

        SpaceCoordinates latLong = spaceResponse.iss_position;
        latitude = latLong.latitude;
        longitude = latLong.longitude;

    }
}
