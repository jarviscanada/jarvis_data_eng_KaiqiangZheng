package ca.jrvs.apps.stockquote.data.utils;

//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//import java.io.IOException;
//
//public class QuoteHttpHelper {
//
//  private final String apiKey;
//  private final OkHttpClient client;
//  private final ObjectMapper mapper;
//
//  public QuoteHttpHelper(String apiKey) {
//    this.apiKey = apiKey;
//    this.client = new OkHttpClient();
//    this.mapper = new ObjectMapper();
//  }
//
//  public JsonNode fetchQuoteInfo(String symbol) throws IllegalArgumentException {
//    String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";
//
//    Request request = new Request.Builder()
//        .url(url)
//        .addHeader("X-RapidAPI-Key", apiKey)
//        .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
//        .build();
//
//    try (Response response = client.newCall(request).execute()) {
//      if (!response.isSuccessful()) {
//        throw new IllegalArgumentException("Failed to fetch quote for symbol: " + symbol);
//      }
//
//      String body = response.body().string();
//      JsonNode jsonNode = mapper.readTree(body);
//      JsonNode globalQuote = jsonNode.get("Global Quote");
//
//      if (globalQuote == null || globalQuote.isEmpty()) {
//        throw new IllegalArgumentException("No data found for symbol: " + symbol);
//      }
//
//      return globalQuote;
//
//    } catch (IOException e) {
//      throw new RuntimeException("Error fetching quote: " + e.getMessage(), e);
//    }
//  }
//}


import ca.jrvs.apps.stockquote.data.entity.Quote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Timestamp;

public class QuoteHttpHelper {

  private String apiKey;
  private OkHttpClient client;
  private ObjectMapper mapper;

  public QuoteHttpHelper(String apiKey, OkHttpClient client) {
    this.apiKey = apiKey;
    this.client = client;
    this.mapper = new ObjectMapper();
  }

  /**
   * Fetch latest quote data from Alpha Vantage endpoint
   * @param symbol
   * @return Quote with latest data
   * @throws IllegalArgumentException - if no data was found for the given symbol
   */
  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
    // 1. Build the URL (use GLOBAL_QUOTE)
    // The URL format depends on whether you're using the official API or RapidAPI
    // If you're using RapidAPI, don't forget to add the header as you did in the exercises
    // The following shows the official API call method, adjust the Request.Builder for RapidAPI if needed
    //String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
    String url = "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json";

    Request request = new Request.Builder()
        .url(url)
        .addHeader("X-RapidAPI-Key", apiKey)
        .addHeader("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
        .get()
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IllegalArgumentException("HTTP request failed: " + response.code());
      }

      if (response.body() == null) {
        throw new IllegalArgumentException("Response body is null");
      }

      // 2. Parse the JSON
      String jsonStr = response.body().string();

      // 3. Handle the nested structure "Global Quote"
      // readTree converts the JSON into a tree, and we can access the data at will
      JsonNode root = mapper.readTree(jsonStr);
      JsonNode quoteNode = root.get("Global Quote");

      if (quoteNode == null || quoteNode.isEmpty()) {
        throw new IllegalArgumentException("Invalid stock symbol or API limit: " + symbol);
      }

      // 4. Convert the contents inside "Global Quote" to a Quote object
      // treeToValue is a powerful feature of Jackson that converts a branch of the tree into an object
      Quote quote = mapper.treeToValue(quoteNode, Quote.class);

      // 5. Manually set the Timestamp (because the API doesn't return it, but the database requires it)
      quote.setTimestamp(new Timestamp(System.currentTimeMillis()));

      return quote;

    } catch (IOException e) {
      throw new RuntimeException("Data processing failed", e);
    }
  }


//    public static void main(String[] args) {
//      // API Key
//      String apiKey = "a3dbdd601fmshff8d40763377696p187a68jsnbc83700e5817";
//      OkHttpClient client = new OkHttpClient();
//      QuoteHttpHelper helper = new QuoteHttpHelper(apiKey, client);
//
//      try {
//        Quote quote = helper.fetchQuoteInfo("MSFT");
//        System.out.println("Successfully fetched stock: " + quote.getTicker());
//        System.out.println("Current price: " + quote.getPrice());
//        System.out.println("Fetched at: " + quote.getTimestamp());
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//
//  }
}