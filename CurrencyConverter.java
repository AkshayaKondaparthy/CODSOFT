import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    // Method to fetch the exchange rate from the API
    public static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        double rate = 0.0;
        try {
            // API URL for the given base currency
            String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the API response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Extract the target currency rate from the JSON response.
                // Note: This is a basic extraction and assumes the response format remains consistent.
                String json = response.toString();
                String searchString = "\"" + targetCurrency + "\":";
                int index = json.indexOf(searchString);
                if (index != -1) {
                    int start = index + searchString.length();
                    int end = json.indexOf(",", start);
                    if (end == -1) {
                        end = json.indexOf("}", start);
                    }
                    String rateStr = json.substring(start, end).trim();
                    rate = Double.parseDouble(rateStr);
                } else {
                    System.out.println("Target currency not found in the API response.");
                }
            } else {
                System.out.println("GET request failed. Response code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
        }
        return rate;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Currency selection
        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Amount input
        System.out.print("Enter amount in " + baseCurrency + ": ");
        double amount = scanner.nextDouble();

        // Fetch currency rate and perform conversion
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);
        if (exchangeRate == 0.0) {
            System.out.println("Conversion rate not available.");
        } else {
            double convertedAmount = amount * exchangeRate;
            // Display result with the target currency symbol/code
            System.out.println(amount + " " + baseCurrency + " = " + convertedAmount + " " + targetCurrency);
        }
        scanner.close();
    }
}
