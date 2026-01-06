package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Position;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import java.util.Optional;

public class PositionService {

  private PositionDao positionDao;
  private QuoteDao quoteDao;

  public PositionService(PositionDao positionDao, QuoteDao quoteDao) {
    this.positionDao = positionDao;
    this.quoteDao = quoteDao;
  }

  /**
   * Process a buy order.
   * Logic:
   * 1. Check if there is a quote (Quote) for the stock in the database.
   * 2. Check if the number of shares to be bought does not exceed the stock's available volume (Volume) -> Simple risk control logic.
   * 3. Calculate the new position data (if there is an existing position, it should be accumulated).
   * 4. Save the new position.
   */
  public Position buy(String ticker, int numberOfShares, double price) {
    // 1. Check if the stock exists
    Optional<Quote> quoteOptional = quoteDao.findById(ticker);
    //if (quoteOptional.isEmpty()) Java 11
    if (!quoteOptional.isPresent()) {
      throw new IllegalArgumentException("Ticker not found in database: " + ticker);
    }
    Quote quote = quoteOptional.get();

    // 2. Check Volume (business logic requirement)
    if (numberOfShares > quote.getVolume()) {
      throw new IllegalArgumentException("Cannot buy " + numberOfShares + " shares. Only " + quote.getVolume() + " available.");
    }

    // 3. Calculate new position
    // First, check if there is an existing position
    Optional<Position> existingPos = positionDao.findById(ticker);
    Position position;

    if (existingPos.isPresent()) {
      position = existingPos.get();
      // Accumulate the number of shares
      position.setNumOfShares(position.getNumOfShares() + numberOfShares);
      // Accumulate the value paid (old total value paid + this purchase's value)
      position.setValuePaid(position.getValuePaid() + (numberOfShares * price));
    } else {
      // Create a new position
      position = new Position();
      position.setTicker(ticker);
      position.setNumOfShares(numberOfShares);
      position.setValuePaid(numberOfShares * price);
    }

    // 4. Save
    return positionDao.save(position);
  }

  /**
   * Sell all stocks
   * Logic update: Must first check if the position exists
   */
  public void sell(String ticker) {
    // 1. Check if the position exists
    Optional<Position> position = positionDao.findById(ticker);

    // 2. If not found (Empty), it means the user hasn't bought any, throw an exception
    if (!position.isPresent()) {
      throw new IllegalArgumentException("Cannot sell: You do not currently own any stocks of " + ticker + ".");
    }

    // 3. If found, proceed with deletion
    positionDao.deleteById(ticker);
  }

  /**
   * @return positionDao.findAll();  //showing all positions
   */
  public Iterable<Position> findAllPositions() {
    return positionDao.findAll();
  }
}
