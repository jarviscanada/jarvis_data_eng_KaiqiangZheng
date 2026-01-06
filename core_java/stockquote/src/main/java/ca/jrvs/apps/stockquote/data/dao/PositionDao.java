package ca.jrvs.apps.stockquote.data.dao;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import ca.jrvs.apps.stockquote.data.entity.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionDao implements CrudDao<Position, String> {

//  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);
  private Connection c;

  public PositionDao(Connection c) {
    this.c = c;
  }

  @Override
  public Position save(Position entity) {
    // Use UPSERT (Insert or Update)
    String sql = "INSERT INTO position (symbol, number_of_shares, value_paid) " +
        "VALUES (?, ?, ?) " +
        "ON CONFLICT (symbol) DO UPDATE " +
        "SET number_of_shares = ?, value_paid = ?";

    try (PreparedStatement statement = c.prepareStatement(sql)) {
      // INSERT Part
      statement.setString(1, entity.getTicker());
      statement.setInt(2, entity.getNumOfShares());
      statement.setDouble(3, entity.getValuePaid());

      // UPDATE Part
      statement.setInt(4, entity.getNumOfShares());
      statement.setDouble(5, entity.getValuePaid());

      statement.executeUpdate();
      return entity;
    } catch (SQLException e) {
      throw new RuntimeException("Unable to save Position: " + entity.getTicker(), e);
    }
  }

  @Override
  public Optional<Position> findById(String symbol) {
    String sql = "SELECT * FROM position WHERE symbol = ?";
    try (PreparedStatement statement = c.prepareStatement(sql)) {
      statement.setString(1, symbol);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return Optional.of(mapResultSetToPosition(rs));
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException("Unable to find Position: " + symbol, e);
    }
  }

  @Override
  public Iterable<Position> findAll() {
    String sql = "SELECT * FROM position";
    List<Position> positions = new ArrayList<>();
    try (PreparedStatement statement = c.prepareStatement(sql)) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        positions.add(mapResultSetToPosition(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Unable to retrieve all Positions", e);
    }
    return positions;
  }

  @Override
  public void deleteById(String symbol) {
    String sql = "DELETE FROM position WHERE symbol = ?";
    try (PreparedStatement statement = c.prepareStatement(sql)) {
      statement.setString(1, symbol);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Unable to delete Position: " + symbol, e);
    }
  }

  @Override
  public void deleteAll() {
    // Note: Since no other tables depend on position, we can delete directly
    String sql = "DELETE FROM position";
    try (PreparedStatement statement = c.prepareStatement(sql)) {
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Unable to delete all Positions", e);
    }
  }

  // Helper method: Map ResultSet to Position object
  private Position mapResultSetToPosition(ResultSet rs) throws SQLException {
    Position p = new Position();
    p.setTicker(rs.getString("symbol"));
    p.setNumOfShares(rs.getInt("number_of_shares"));
    p.setValuePaid(rs.getDouble("value_paid"));
    return p;
  }
}
