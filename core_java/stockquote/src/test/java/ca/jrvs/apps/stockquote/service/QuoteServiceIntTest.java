package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.data.utils.QuoteHttpHelper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class QuoteServiceIntTest {

  private QuoteService quoteService;
  private QuoteDao quoteDao;
  private PositionDao positionDao; //new add
  private Connection connection;

  @BeforeEach
  public void setUp() throws SQLException {
    // 1. Real DB Connection
    String url = "jdbc:postgresql://localhost:5432/stock_quote";
    String user = "postgres"; // USERNAME
    String password = "password"; // PASSWORD
    connection = DriverManager.getConnection(url, user, password);
    quoteDao = new QuoteDao(connection);
    positionDao = new PositionDao(connection); //Initialize PositionDao

    // 2. Real HttpHelper
    String apiKey = "a3dbdd601fmshff8d40763377696p187a68jsnbc83700e5817"; // YOUR API KEY
    OkHttpClient client = new OkHttpClient();
    QuoteHttpHelper httpHelper = new QuoteHttpHelper(apiKey, client);

    // 3. Real Service
    quoteService = new QuoteService(quoteDao, httpHelper);

    // Clean up
    // First delete from the child table (Position), then delete from the parent table (Quote)

    try {
      positionDao.deleteAll();
    } catch (Exception e) {
      // Ignore the error to prevent blocking the process in case the Position table is empty or other exceptions occur, or handle it based on actual needs
    }
    quoteDao.deleteAll();
  }

  @Test
  public void integrationTest() {
    String ticker = "MSFT";

    // 1. Call the Service to fetch data from the web and save it to the database
    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    // 2. Verify the Service's return value
    assertTrue(result.isPresent());
    assertEquals(ticker, result.get().getTicker());

    // 3. Verify that the data has actually been saved to the database
    Optional<Quote> dbResult = quoteDao.findById(ticker);
    assertTrue(dbResult.isPresent());
    assertEquals(ticker, dbResult.get().getTicker());

    System.out.println("Integration test passed: MSFT has been downloaded and saved to the database");
  }
}

