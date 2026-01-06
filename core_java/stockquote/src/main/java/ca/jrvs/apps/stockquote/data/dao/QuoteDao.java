package ca.jrvs.apps.stockquote.data.dao;

import ca.jrvs.apps.stockquote.data.entity.Quote;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteDao implements CrudDao<Quote, String> {

  // Use slf4j to log messages, useful for debugging
  //private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);

  private Connection c;

  public QuoteDao(Connection c) {
    this.c = c;
  }

  @Override
  public Quote save(Quote quote) {
    String upsertSql = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
        "ON CONFLICT (symbol) DO UPDATE " +
        "SET open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ?";

    // Note: The SQL above uses PostgreSQL-specific UPSERT syntax (ON CONFLICT).
    // This is the most concise way. If you're using MySQL, the syntax is "ON DUPLICATE KEY UPDATE ..."

    try (PreparedStatement statement = c.prepareStatement(upsertSql)) {
      // --- Fill parameters for the INSERT part (1-11) ---
      statement.setString(1, quote.getTicker());
      statement.setDouble(2, quote.getOpen());
      statement.setDouble(3, quote.getHigh());
      statement.setDouble(4, quote.getLow());
      statement.setDouble(5, quote.getPrice());
      statement.setInt(6, quote.getVolume());
      statement.setDate(7, quote.getLatestTradingDay());
      statement.setDouble(8, quote.getPreviousClose());
      statement.setDouble(9, quote.getChange());
      statement.setString(10, quote.getChangePercent());
      statement.setTimestamp(11, quote.getTimestamp());

      // --- Fill parameters for the UPDATE part (12-21) ---
      statement.setDouble(12, quote.getOpen());
      statement.setDouble(13, quote.getHigh());
      statement.setDouble(14, quote.getLow());
      statement.setDouble(15, quote.getPrice());
      statement.setInt(16, quote.getVolume());
      statement.setDate(17, quote.getLatestTradingDay());
      statement.setDouble(18, quote.getPreviousClose());
      statement.setDouble(19, quote.getChange());
      statement.setString(20, quote.getChangePercent());
      statement.setTimestamp(21, quote.getTimestamp());

      statement.executeUpdate();
      return quote;

    } catch (SQLException e) {
      throw new RuntimeException("Unable to save Quote: " + quote.getTicker(), e);
    }
  }

  @Override
  public Optional<Quote> findById(String symbol) {
    String selectSql = "SELECT * FROM quote WHERE symbol = ?";

    try (PreparedStatement statement = c.prepareStatement(selectSql)) {
      statement.setString(1, symbol);

      // Execute the query
      ResultSet rs = statement.executeQuery();

      // If the result set has the next row, data has been found
      if (rs.next()) {
        Quote quote = mapResultSetToQuote(rs);
        return Optional.of(quote);
      } else {
        return Optional.empty();
      }

    } catch (SQLException e) {
      throw new RuntimeException("Unable to find Quote: " + symbol, e);
    }
  }

  @Override
  public Iterable<Quote> findAll() {
    String selectAllSql = "SELECT * FROM quote";
    List<Quote> quotes = new ArrayList<>();

    try (PreparedStatement statement = c.prepareStatement(selectAllSql)) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        quotes.add(mapResultSetToQuote(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Unable to fetch all Quotes", e);
    }
    return quotes;
  }

  @Override
  public void deleteById(String symbol) {
    String deleteSql = "DELETE FROM quote WHERE symbol = ?";
    try (PreparedStatement statement = c.prepareStatement(deleteSql)) {
      statement.setString(1, symbol);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Unable to delete Quote: " + symbol, e);
    }
  }

  @Override
  public void deleteAll() {
    String deleteAllSql = "DELETE FROM quote";
    try (PreparedStatement statement = c.prepareStatement(deleteAllSql)) {
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Unable to delete all Quotes", e);
    }
  }

  // This is a helper method to convert a single row of data from the database (ResultSet) into a Java object
  // Avoids code repetition
  private Quote mapResultSetToQuote(ResultSet rs) throws SQLException {
    Quote quote = new Quote();
    quote.setTicker(rs.getString("symbol"));
    quote.setOpen(rs.getDouble("open"));
    quote.setHigh(rs.getDouble("high"));
    quote.setLow(rs.getDouble("low"));
    quote.setPrice(rs.getDouble("price"));
    quote.setVolume(rs.getInt("volume"));
    quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
    quote.setPreviousClose(rs.getDouble("previous_close"));
    quote.setChange(rs.getDouble("change"));
    quote.setChangePercent(rs.getString("change_percent"));
    quote.setTimestamp(rs.getTimestamp("timestamp"));
    return quote;
  }
}