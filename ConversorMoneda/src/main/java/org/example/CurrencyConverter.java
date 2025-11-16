package org.example;
// codigo conversor moneda, primero se manda a llamar las librerias JSON

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//pantalla de programa inicial de conversor
public class CurrencyConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CONVERSOR DE MONEDA ===");
        System.out.print("Ingrese la moneda base (por ejemplo, USD): ");
        String fromCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la moneda destino (por ejemplo, MXN): ");
        String toCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la cantidad a convertir: ");
        double amount = scanner.nextDouble();

        try {
            // integración personalintegración de mi API
            String apiKey = "574c1d024d74479882525c05";
            String urlStr = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + fromCurrency;

            URL url = new URL(urlStr);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Leer la respuesta JSON usando Gson
            JsonObject json = JsonParser.parseReader(new InputStreamReader(request.getInputStream())).getAsJsonObject();

            // Ver que funcione el API
            String result = json.get("result").getAsString();
            if (!result.equals("success")) {
                System.out.println("Error en la respuesta de la API: " + json);
                return;
            }

            // Obtener tasas de conversión
            JsonObject conversionRates = json.getAsJsonObject("conversion_rates");

            if (!conversionRates.has(toCurrency)) {
                System.out.println("Moneda no encontrada: " + toCurrency);
                return;
            }

            double exchangeRate = conversionRates.get(toCurrency).getAsDouble();
            double convertedAmount = amount * exchangeRate;

            System.out.printf("\n%.2f %s = %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);

        } catch (Exception e) {
            System.out.println("Error al realizar la conversión: " + e.getMessage());
        }

        scanner.close();
    }
}
