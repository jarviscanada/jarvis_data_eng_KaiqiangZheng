package ca.jrvs.apps.stockquote.data.dao;


import ca.jrvs.apps.stockquote.data.entity.Quote;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class QuoteDaoTest {

  private QuoteDao quoteDao;
  private Connection connection;

  @BeforeEach
  public void setUp() throws SQLException {
    // 1. Get database connection
    // The url, user, password below to match psql environment
    String url = "jdbc:postgresql://localhost:5432/stock_quote";
    String user = "postgres";  // Database username
    String password = "password"; // Database password

    connection = DriverManager.getConnection(url, user, password);
    quoteDao = new QuoteDao(connection);

    // Clean up old data to ensure the test environment is clean
    quoteDao.deleteAll();
  }

  @AfterEach
  public void tearDown() throws SQLException {
    // Close the connection after the test is done
    connection.close();
  }

  @Test
  public void saveAndFind() {
    // 1. Create a test Quote object
    Quote quote = new Quote();
    quote.setTicker("AAPL");
    quote.setOpen(100.0);
    quote.setHigh(110.0);
    quote.setLow(90.0);
    quote.setPrice(105.0);
    quote.setVolume(5000);
    quote.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
    quote.setPreviousClose(100.0);
    quote.setChange(5.0);
    quote.setChangePercent("5%");
    quote.setTimestamp(Timestamp.from(Instant.now()));

    // 2. Save it to the database
    quoteDao.save(quote);

    // 3. Retrieve it from the database
    Optional<Quote> result = quoteDao.findById("AAPL");

    // 4. Validate
    assertTrue(result.isPresent());
    assertEquals(105.0, result.get().getPrice());
    assertEquals("AAPL", result.get().getTicker());

    System.out.println("Test passed: Successfully saved and retrieved AAPL with price " + result.get().getPrice());
  }
}
