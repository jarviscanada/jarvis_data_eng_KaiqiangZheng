package ca.jrvs.apps.stockquote.controller;

import ca.jrvs.apps.stockquote.data.entity.Position;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

  private static final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);

  private QuoteService quoteService;
  private PositionService positionService;

  public StockQuoteController(QuoteService quoteService, PositionService positionService) {
    this.quoteService = quoteService;
    this.positionService = positionService;
  }

  /**
   * Start the client interface
   */
  public void initClient() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to the Jarvis Stock Trading System!");
    System.out.println("Available commands: quote [symbol] | buy [symbol] [shares] | sell [symbol] | view | quit");

    while (true) {
      System.out.print("> ");
      String input = scanner.nextLine();
      String[] tokens = input.trim().split("\\s+"); // Split by space

      if (tokens.length == 0) continue;

      String command = tokens[0].toLowerCase();

      try {
        switch (command) {
          case "quote":
            handleQuote(tokens);
            break;
          case "buy":
            handleBuy(tokens);
            break;
          case "sell":
            handleSell(tokens);
            break;
          case "view":
            handleView();
            break;
          case "quit":
          case "exit":
            System.out.println("Exiting the system. Goodbye!");
            return;
          default:
            System.out.println("Invalid command. Please try: quote, buy, sell, view, quit");
        }
      } catch (Exception e) {
        logger.error("Error processing command: " + command, e);
        System.out.println("Operation failed: " + e.getMessage());
      }
    }
  }

  private void handleQuote(String[] tokens) {
    if (tokens.length != 2) {
      System.out.println("Usage: quote [symbol]");
      return;
    }
    String symbol = tokens[1].toUpperCase();
    Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(symbol);
    if (quote.isPresent()) {
      Quote q = quote.get();
      System.out.printf("Stock: %s | Price: %.2f | Previous Close: %.2f | Change: %s%n",
          q.getTicker(), q.getPrice(), q.getPreviousClose(), q.getChangePercent());
    } else {
      System.out.println("No information found for this stock.");
    }
  }

  private void handleBuy(String[] tokens) {
    if (tokens.length != 3) {
      System.out.println("Usage: buy [symbol] [shares]");
      return;
    }
    String symbol = tokens[1].toUpperCase();
    try {
      int shares = Integer.parseInt(tokens[2]);
      // To buy, we need to get the latest price first
      Optional<Quote> quoteOpt = quoteService.fetchQuoteDataFromAPI(symbol);
      //if (quoteOpt.isEmpty()) {  java 11
      if (!quoteOpt.isPresent()) {
        System.out.println("Unable to fetch stock price, cannot buy.");
        return;
      }
      double price = quoteOpt.get().getPrice();

      Position pos = positionService.buy(symbol, shares, price);
      System.out.printf("Successfully bought! Current position: %s, Shares: %d, Total cost: %.2f%n",
          pos.getTicker(), pos.getNumOfShares(), pos.getValuePaid());

      logger.info("User bought " + shares + " shares of " + symbol);

    } catch (NumberFormatException e) {
      System.out.println("Error: Shares must be an integer.");
    }
  }

  private void handleSell(String[] tokens) {
    if (tokens.length != 2) {
      System.out.println("Usage: sell [symbol]");
      return;
    }
    String symbol = tokens[1].toUpperCase();
    positionService.sell(symbol);
    System.out.println("Sold all " + symbol + " stocks.");
    logger.info("User sold all shares of " + symbol);
  }

  private void handleView() {
    System.out.println("--- Your Positions ---");
    // Assuming you added a findAllPositions method in PositionService
    // If not, this will cause an error. Ensure Phase 3 tasks are complete.
    Iterable<Position> positions = positionService.findAllPositions();

    for (Position p : positions) {
      // Get the current price to calculate profit/loss (optional feature)
      Optional<Quote> q = quoteService.fetchQuoteDataFromAPI(p.getTicker());
      double currentPrice = q.isPresent() ? q.get().getPrice() : 0.0;
      double marketValue = currentPrice * p.getNumOfShares();
      double profit = marketValue - p.getValuePaid();

      System.out.printf("Symbol: %-5s | Shares: %-4d | Cost: %-10.2f | Market Value: %-10.2f | Profit/Loss: %.2f%n",
          p.getTicker(), p.getNumOfShares(), p.getValuePaid(), marketValue, profit);
    }
    System.out.println("-----------------------");
  }
}
