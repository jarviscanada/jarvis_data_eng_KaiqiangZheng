package ca.jrvs.apps.stockquote.data.dao;

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

public class PositionDaoTest {

  private PositionDao positionDao;
  private QuoteDao quoteDao; // We need this to save a Quote first
  private Connection connection;

  @BeforeEach
  public void setUp() throws SQLException {
    String url = "jdbc:postgresql://localhost:5432/stock_quote";
    String user = "postgres"; // psql username
    String password = "password"; // psql password

    connection = DriverManager.getConnection(url, user, password);
    positionDao = new PositionDao(connection);
    quoteDao = new QuoteDao(connection);

    // The order of cleanup is important: delete the dependent table (Position) first, then the referenced table (Quote)
    positionDao.deleteAll();
    quoteDao.deleteAll();

    // --- Setup: Save a Quote first (MSFT) ---
    Quote msft = new Quote();
    msft.setTicker("MSFT");
    msft.setOpen(300);
    msft.setHigh(310);
    msft.setLow(290);
    msft.setPrice(305);
    msft.setVolume(1000);
    msft.setLatestTradingDay(new java.sql.Date(System.currentTimeMillis()));
    msft.setPreviousClose(300);
    msft.setChange(5);
    msft.setChangePercent("1.5%");
    msft.setTimestamp(Timestamp.from(Instant.now()));
    quoteDao.save(msft);
  }

  @AfterEach
  public void tearDown() throws SQLException {
    connection.close();
  }

  @Test
  public void saveAndFind() {
    // 1. Create Position
    Position pos = new Position();
    pos.setTicker("MSFT"); // Must match the MSFT saved earlier
    pos.setNumOfShares(100);
    pos.setValuePaid(30500.00); // Assuming purchase price of 305 * 100

    // 2. Save
    positionDao.save(pos);

    // 3. Find
    Optional<Position> result = positionDao.findById("MSFT");

    // 4. Verify
    assertTrue(result.isPresent());
    assertEquals(100, result.get().getNumOfShares());
    assertEquals(30500.00, result.get().getValuePaid());

    System.out.println("Position test passed! Position value: " + result.get().getValuePaid());
  }

  @Test
  public void deleteById() {
    // 1. Save
    Position pos = new Position();
    pos.setTicker("MSFT");
    pos.setNumOfShares(50);
    pos.setValuePaid(15000);
    positionDao.save(pos);

    // 2. Delete
    positionDao.deleteById("MSFT");

    // 3. Verify deletion
    Optional<Position> result = positionDao.findById("MSFT");
    assertFalse(result.isPresent());
  }
}
