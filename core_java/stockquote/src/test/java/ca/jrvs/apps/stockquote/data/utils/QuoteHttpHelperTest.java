package ca.jrvs.apps.stockquote.data.utils;

import ca.jrvs.apps.stockquote.data.entity.Quote;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuoteHttpHelperTest {

  private QuoteHttpHelper helper;

  @BeforeEach
  public void setUp() {
    // This method will run before each @Test
    // Here we initialize the helper
    String apiKey = "a3dbdd601fmshff8d40763377696p187a68jsnbc83700e5817"; // ¼ÇµÃÌîÄãµÄ Key
    OkHttpClient client = new OkHttpClient();
    helper = new QuoteHttpHelper(apiKey, client);
  }

  @Test
  public void fetchQuoteInfo_Success() {
    // 1. Execute
    Quote quote = helper.fetchQuoteInfo("MSFT");

    // 2. Verify (Assert)
    // Instead of checking console prints, we use code to assert the results
    assertNotNull(quote, "The returned Quote object should not be null");
    assertEquals("MSFT", quote.getTicker(), "The stock ticker should be MSFT");
    assertTrue(quote.getPrice() > 0, "The stock price should be greater than 0");
    assertNotNull(quote.getTimestamp(), "The timestamp should not be null");


    System.out.println("Test passed! Current price: " + quote.getPrice());
  }

  @Test
  public void fetchQuoteInfo_Failure() {
    // Test a non-existing stock symbol, it should throw an exception
    assertThrows(IllegalArgumentException.class, () -> {
      helper.fetchQuoteInfo("INVALID_STOCK_CODE_123");
    });
  }
}