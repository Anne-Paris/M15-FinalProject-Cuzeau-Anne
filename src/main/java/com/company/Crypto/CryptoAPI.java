package com.company.Crypto;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class CryptoAPI {

    public static CryptoResponse[] getCoinValue(String assetID) {

        //Create URI string
        String apiKey ="?apikey=2F7AD425-7BAE-48AD-A9D3-D3042E04FA46";
        String URI = "https://rest.coinapi.io/v1/assets/" + assetID + apiKey;
        WebClient client = WebClient.create(URI);
        CryptoResponse[] cryptoResponse = null;

        //Try request to API
        try {
            Mono<CryptoResponse[]> response = client
                    .get()
                    .retrieve()
                    .bodyToMono(CryptoResponse[].class).log();
           cryptoResponse = response.share().block();

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
        return cryptoResponse;
    }

    //Fetches the currency name, If null, returns an error String.
    public static String displayName(CryptoResponse[] cryptoResponse){
        if (cryptoResponse == null || cryptoResponse.length == 0){
            String error = "There were errors";
            return error;
        }

        else {
            String name = cryptoResponse[0].name;
            return name;}
    }

    //Fetches the Asset ID. Error checking in App.
    public static String displayAsset_ID(CryptoResponse[] cryptoResponse){
        String asset_id = cryptoResponse[0].asset_id;
        return asset_id;
    }

    //Fetches the price in USD. Error checking in App.
    public static double displayUSD_Price(CryptoResponse[] cryptoResponse){
        double price = cryptoResponse[0].price_usd;
        return price;
    }
}
