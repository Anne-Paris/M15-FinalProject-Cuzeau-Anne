package com.company;

import com.company.Crypto.CryptoAPI;
import com.company.Crypto.CryptoResponse;
import com.company.Space.SpaceAPI;
import com.company.Weather.WeatherAPI;
import com.company.Weather.WeatherResponse;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String choiceStr;
		int choice;

		do {
			// Display the menu
			System.out.println("\n" + "Please select what you'd like to do: ");
			System.out.println("1\t Weather in a city");
			System.out.println("2\t Weather at the ISS coordinates.");
			System.out.println("3\t Print city and country based on ISS coordinates.");
			System.out.println("4\t Find out US price of Crypto currency");
			System.out.println("5\t Exit App");
			System.out.println("Please enter your choice:");

			//Get user's choice
			choiceStr = input.nextLine();
			while (choiceStr == null || choiceStr.matches("^[1-5]$") == false) {
				System.out.println("Invalid choice, please try again");
				System.out.println("Please enter your choice:");
				choiceStr = input.nextLine();
			}

			choice = Integer.parseInt(choiceStr);
			try {
				switch (choice) {
					case 1:
						case1(input);
						break;
					case 2:
						case2();
						break;
					case 3:
						case3();
						break;
					case 4:
						case4(input);
						break;
					case 5:
						System.out.println("You want to exit");
						System.out.println("Thank you, bye!");
						break;
					default:
						while (choiceStr == null || choiceStr.matches("^[1-5]$") == false) {
							System.out.println("Invalid choice of number, please try again");
							System.out.println("Please enter your choice:");
							choiceStr = input.nextLine();
							choice = Integer.parseInt(choiceStr);
						}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Sorry, your input is not correct. Please try again:");
				choiceStr = input.nextLine();
				choice = Integer.parseInt(choiceStr);
			} catch (NumberFormatException e) {
				System.out.print("Your selection can only be an integer!");
				choiceStr = input.nextLine();
				choice = Integer.parseInt(choiceStr);
			}


		} while (choice != 5);
	}

	private static void case4(Scanner input) {
		CryptoAPI cryptoAPI = new CryptoAPI();
		System.out.print("Please enter the currency symbol you want to see: ");
		String choiceCryptoCurrency = input.nextLine();
		CryptoResponse[] cryptoResponse = CryptoAPI.getCoinValue(choiceCryptoCurrency);
		while (cryptoResponse == null || cryptoResponse.length == 0) {
			System.out.print("This currency does not exist, please try another currency:");
			choiceCryptoCurrency = input.nextLine();
			cryptoResponse = CryptoAPI.getCoinValue(choiceCryptoCurrency);
		}
		System.out.println(cryptoAPI.displayName(cryptoResponse));
		System.out.println(cryptoAPI.displayAsset_ID(cryptoResponse));
		System.out.println("USD price: $"+ String.format("%,.2f",cryptoAPI.displayUSD_Price(cryptoResponse)));
		String.format("%n");
	}


	private static void case1(Scanner input) {
		System.out.println("Please enter your city:");
		String city = input.nextLine();
		WeatherAPI weatherAPI = new WeatherAPI();
		String request = weatherAPI.getWeatherAPIRequest(city);
		WeatherResponse weather = weatherAPI.getWeather(request);
		while (weather == null){
			System.out.println("Please re-enter your city: ");
			city = input.nextLine();
			request = weatherAPI.getWeatherAPIRequest(city);
			weather = weatherAPI.getWeather(request);
		}
		weatherAPI.displayWeather(weather);
	}

	private static void case2() {
		SpaceAPI spaceAPI = new SpaceAPI();
		WeatherAPI weatherAPIiss = new WeatherAPI();
		double issLon = spaceAPI.getISSlongitude();
		double issLat = spaceAPI.getISSlatitude();
		String request2 = weatherAPIiss.getWeatherAPIRequest(issLat, issLon);
		WeatherResponse weather2 = weatherAPIiss.getWeather(request2);
		weatherAPIiss.displayWeather(weather2);
	}

	private static void case3() {
		SpaceAPI spaceAPI2 = new SpaceAPI();
		WeatherAPI weatherAPIissLoc = new WeatherAPI();
		double issLonLoc = spaceAPI2.getISSlongitude();
		double issLatLoc = spaceAPI2.getISSlatitude();
		String displayLoc = weatherAPIissLoc.getWeatherAPIRequest(issLatLoc, issLonLoc);
		WeatherResponse ISSLoc = weatherAPIissLoc.getWeather(displayLoc);
		weatherAPIissLoc.displayLocation(ISSLoc);
	}
}