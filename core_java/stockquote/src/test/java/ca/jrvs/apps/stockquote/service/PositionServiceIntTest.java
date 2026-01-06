package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Position;
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

public class PositionServiceIntTest {

  private PositionService positionService;
  private PositionDao positionDao;
  private QuoteDao quoteDao;
  private Connection connection;

  @BeforeEach
  public void setUp() throws SQLException {
    // 1. Set up database connection (replace with the actual username and password)
    String url = "jdbc:postgresql://localhost:5432/stock_quote";
    String user = "postgres";
    String password = "password";
    connection = DriverManager.getConnection(url, user, password);

    // 2. Initialize DAO and Service
    quoteDao = new QuoteDao(connection);
    positionDao = new PositionDao(connection);
    positionService = new PositionService(positionDao, quoteDao);

    // 3. Clean up environment (order is important: delete Position with foreign key first, then delete Quote)
    positionDao.deleteAll();
    quoteDao.deleteAll();
  }

  @AfterEach
  public void tearDown() throws SQLException {
    connection.close();
  }

  @Test
  public void buy_Success() {
    // --- Setup ---
    // Must have a Quote in the database, otherwise buying will throw a foreign key error
    Quote apple = new Quote();
    apple.setTicker("AAPL");
    apple.setPrice(150.0);
    apple.setVolume(5000); // Sufficient stock available

    // Fill in required fields to satisfy database constraints
    apple.setOpen(149.0);
    apple.setHigh(151.0);
    apple.setLow(148.0);
    apple.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
    apple.setPreviousClose(148.0);
    apple.setChange(2.0);
    apple.setChangePercent("1.3%");
    apple.setTimestamp(Timestamp.from(Instant.now()));

    quoteDao.save(apple);

    // --- Execute test: Buy ---
    // Buy 10 shares at price 150
    Position pos = positionService.buy("AAPL", 10, 150.0);

    // --- Verify ---
    assertEquals("AAPL", pos.getTicker());
    assertEquals(10, pos.getNumOfShares());
    assertEquals(1500.0, pos.getValuePaid());

    // Verify that the position is actually saved in the database
    Optional<Position> storedPos = positionDao.findById("AAPL");
    assertTrue(storedPos.isPresent());
    assertEquals(10, storedPos.get().getNumOfShares());
  }

  @Test
  public void buy_Fail_NotEnoughVolume() {
    // --- Setup ---
    Quote gme = new Quote();
    gme.setTicker("GME");
    gme.setPrice(20.0);
    gme.setVolume(5); // Very low stock volume, only 5 shares available

    // Fill in other required fields
    gme.setOpen(20.0); gme.setHigh(20.0); gme.setLow(20.0);
    gme.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
    gme.setPreviousClose(20.0); gme.setChange(0.0); gme.setChangePercent("0%");
    gme.setTimestamp(Timestamp.from(Instant.now()));

    quoteDao.save(gme);

    // --- Execute test ---
    // Attempt to buy 100 shares (exceeds available stock of 5)
    // Expected to throw IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> {
      positionService.buy("GME", 100, 20.0);
    });
  }

  @Test
  public void sell_Success() {
    // --- Setup ---
    // 1. Save Quote
    Quote tsla = new Quote();
    tsla.setTicker("TSLA");
    tsla.setPrice(800.0); tsla.setVolume(1000);
    tsla.setOpen(800.0); tsla.setHigh(800.0); tsla.setLow(800.0);
    tsla.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
    tsla.setPreviousClose(800.0); tsla.setChange(0.0); tsla.setChangePercent("0%");
    tsla.setTimestamp(Timestamp.from(Instant.now()));
    quoteDao.save(tsla);

    // 2. Buy (create position)
    positionService.buy("TSLA", 5, 800.0);
    assertTrue(positionDao.findById("TSLA").isPresent());

    // --- Execute test: Sell ---
    positionService.sell("TSLA");

    // --- Verify ---
    // After selling, there should be no TSLA in the Position table
    //assertTrue(positionDao.findById("TSLA").isEmpty()); Java 11
    assertTrue(!positionDao.findById("TSLA").isPresent());
  }
}

