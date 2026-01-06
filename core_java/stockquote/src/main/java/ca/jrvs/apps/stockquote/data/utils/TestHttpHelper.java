package ca.jrvs.apps.stockquote.data.utils;

import ca.jrvs.apps.stockquote.data.entity.Quote;
import okhttp3.OkHttpClient;

public class TestHttpHelper {
  public static void main(String[] args) {
    // API Key
    String apiKey = "a3dbdd601fmshff8d40763377696p187a68jsnbc83700e5817";
    OkHttpClient client = new OkHttpClient();
    QuoteHttpHelper helper = new QuoteHttpHelper(apiKey, client);

    try {
      Quote quote = helper.fetchQuoteInfo("MSFT");
      System.out.println("Successfully fetched stock: " + quote.getTicker());
      System.out.println("Current price: " + quote.getPrice());
      System.out.println("Fetched at: " + quote.getTimestamp());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}