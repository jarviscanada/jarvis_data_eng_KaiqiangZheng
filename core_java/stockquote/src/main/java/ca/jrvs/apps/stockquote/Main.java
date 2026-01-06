package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.controller.StockQuoteController;
import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.utils.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    // 1. Read the Properties file
    Map<String, String> properties = new HashMap<>();
    //try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
    try (BufferedReader br = new BufferedReader(new FileReader("properties.txt"))) {
        String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty() || line.startsWith("#")) continue; // Skip comments and empty lines
        String[] tokens = line.split("="); // Note: properties are usually separated by '='
        if (tokens.length == 2) {
          properties.put(tokens[0].trim(), tokens[1].trim());
        }
      }
    } catch (IOException e) {
      logger.error("Failed to load properties file", e);
      System.exit(1);
    }

    // 2. Initialize dependencies
    try {
      // Load database driver
      Class.forName(properties.get("db-class"));

      // Create HTTP client
      OkHttpClient client = new OkHttpClient();

      // Establish database connection
      String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/" + properties.get("database");
      Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"));

      // Dependency Injection
      // Like building blocks, pass lower-level components to the upper-level ones
      QuoteDao quoteDao = new QuoteDao(c);
      PositionDao positionDao = new PositionDao(c);
      QuoteHttpHelper httpHelper = new QuoteHttpHelper(properties.get("api-key"), client);

      QuoteService quoteService = new QuoteService(quoteDao, httpHelper);
      PositionService positionService = new PositionService(positionDao, quoteDao);

      // 3. Start the Controller
      StockQuoteController controller = new StockQuoteController(quoteService, positionService);
      controller.initClient(); // The program will loop here until the user enters 'quit'

      // Close connection before exiting
      c.close();

    } catch (ClassNotFoundException | SQLException e) {
      logger.error("Application initialization failed", e);
      e.printStackTrace();
    }
  }
}
