package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.controller.StockQuoteController;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.utils.QuoteHttpHelper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

  private QuoteDao dao;
  private QuoteHttpHelper httpHelper;

  // Constructor injection: forces the caller to provide DAO and Helper
  public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
    this.dao = dao;
    this.httpHelper = httpHelper;
  }

  /**
   * Fetch the latest Quote from the API and save it to the database.
   * Business logic:
   * 1. Attempt to fetch from the network
   * 2. If successful, save to the database
   * 3. Return the Quote
   * 4. If failed (e.g., incorrect stock ticker), return Empty
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    try {
      // 1. Fetch
      Quote quote = httpHelper.fetchQuoteInfo(ticker);

      // 2. Save
      dao.save(quote);

      // 3. Return
      return Optional.of(quote);
    } catch (Exception e) {
      // Log error here if we have a logger
      //System.err.println("Error fetching quote for " + ticker + ": " + e.getMessage());
      logger.error("Error fetching quote for {}: {}", ticker, e.getMessage(), e);
      return Optional.empty();
    }
  }
}
