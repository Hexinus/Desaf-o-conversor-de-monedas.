package org.example;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    // Mi llave API
    private static final String API_KEY = "574c1d024d74479882525c05";

    // Monedas Disponibles
    private static final String[] CURRENCIES = {
            "USD","EUR","MXN","ARS","BRL","CLP","COP","PEN","CAD","AUD","JPY","CNY","GBP","INR","KRW","CHF",
            "RUB","SEK","NOK","NZD","ZAR","HKD","SGD","DKK","PLN","CZK","TRY","AED","SAR","QAR","EGP","THB",
            "IDR","ILS","VND","NGN","UYU","GHS","MAD","KWD","BHD","TWD","MYR","PKR","BDT","HUF","HRK","RON",
            "BGN","DOP","CRC","GTQ","BOB","PYG","NIO","JMD","TTD","ALL","UAH","GEL","ISK","LKR","MUR","OMR",
            "BND","FJD","XAF","XOF","XPF","KES","TZS","UGX","ZMW","MZN","ETB","DZD","LYD","TND","SYP","IRR"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        //pantalla inicial 1 elegir funcionalidad.
        do {
            System.out.println("\n=== CONVERSOR DE MONEDA ===");
            System.out.println("1. Convertir moneda");
            System.out.println("2. Mostrar monedas disponibles");
            System.out.println("3. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();  //la opcion sera igual a la lectura de el input de el teclado.
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {  //interruptor selectoe de opcion, varia la función que llama.
                case 1 -> convertirMoneda(scanner);
                case 2 -> mostrarMonedas();
                case 3 -> System.out.println("¡Gracias por usar el conversor!");
                default -> System.out.println("Opción inválida. Intenta nuevamente.");
            }
        } while (opcion != 3);
    }

    private static void mostrarMonedas() {
        System.out.println("\n--- MONEDAS DISPONIBLES ---");
        int count = 0;
        for (String moneda : CURRENCIES) {
            System.out.printf("%-5s", moneda); // imprime de forma alineada
            count++;
            if (count % 10 == 0) System.out.println(); // salto de línea cada 10
        }
        System.out.println("\nTotal: " + CURRENCIES.length + " monedas.");
    }

    private static void convertirMoneda(Scanner scanner) {
        System.out.print("Introduce el código de la moneda base (por ejemplo USD): ");
        String base = scanner.nextLine().trim().toUpperCase();
        System.out.print("Introduce el código de la moneda destino (por ejemplo MXN): ");
        String destino = scanner.nextLine().trim().toUpperCase();
        System.out.print("Cantidad a convertir: ");
        double cantidad = scanner.nextDouble();
        if (!esMonedaValida(base) || !esMonedaValida(destino)) {
            System.out.println("❌ Una de las monedas no es válida. Usa la opción 2 para ver las disponibles.");
            return;
        }

        try {
            double tasa = obtenerTasaCambio(base, destino);
            double resultado = cantidad * tasa;
            System.out.printf("\n💱 %.2f %s = %.2f %s\n", cantidad, base, resultado, destino);
        } catch (IOException e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
        }
    }

    private static boolean esMonedaValida(String codigo) {
        return Arrays.asList(CURRENCIES).contains(codigo);
    }

    private static double obtenerTasaCambio(String base, String destino) throws IOException {
        String urlStr = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + base;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        Map<?, ?> jsonResponse = gson.fromJson(response.toString(), Map.class);
        Map<String, Double> conversionRates = (Map<String, Double>) jsonResponse.get("conversion_rates");

        return conversionRates.get(destino);
    }
}
