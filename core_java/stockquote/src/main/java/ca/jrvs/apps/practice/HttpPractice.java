package ca.jrvs.apps.practice;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class HttpPractice {

  public static void main(String[] args) {
    // 1. Alpha Vantage API Key
    String apiKey = "a3dbdd601fmshff8d40763377696p187a68jsnbc83700e5817";
    String host = "alpha-vantage.p.rapidapi.com";
    String symbol = "MSFT"; //

    // 2. OkHttp
    OkHttpClient client = new OkHttpClient();

    // 3. URL Alphan vantage directly
    //String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
    //RapidAPI (alpha-vantage like proxy server)
    String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";
    // 4.
    Request request = new Request.Builder()
        .url(url)
        //RapidAPI
        .addHeader("x-rapidapi-host", host)
        .addHeader("x-rapidapi-key", apiKey)
        //
        .get() //
        .build();

    try {
      // 5.
      try (Response response = client.newCall(request).execute()) {
        if (response.isSuccessful() && response.body() != null) {
          //
          String responseBody = response.body().string();
          System.out.println("Status Code: " + response.code());
          System.out.println("Response Body: " + responseBody);
        } else {
          System.out.println("Request failed: " + response.code());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}