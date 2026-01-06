package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Position;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

  @Mock
  private PositionDao positionDao;
  @Mock
  private QuoteDao quoteDao;

  @InjectMocks
  private PositionService positionService;

  @Test
  public void buy_Success() {
    // Arrange
    String ticker = "AAPL";
    int buyShares = 10;
    double price = 150.0;

    Quote mockQuote = new Quote();
    mockQuote.setTicker(ticker);
    mockQuote.setVolume(10000); // Volume here is large enough

    when(quoteDao.findById(ticker)).thenReturn(Optional.of(mockQuote));
    // Simulate no existing position in the database
    when(positionDao.findById(ticker)).thenReturn(Optional.empty());
    // Simulate successful save
    when(positionDao.save(any(Position.class))).thenAnswer(i -> i.getArguments()[0]);

    // Act
    Position result = positionService.buy(ticker, buyShares, price);

    // Assert
    assertNotNull(result);
    assertEquals(10, result.getNumOfShares());
    assertEquals(1500.0, result.getValuePaid());
  }

  @Test
  public void buy_NotEnoughVolume() {
    // Arrange
    String ticker = "GME";
    Quote mockQuote = new Quote();
    mockQuote.setTicker(ticker);
    mockQuote.setVolume(5); // Only 5 shares available

    when(quoteDao.findById(ticker)).thenReturn(Optional.of(mockQuote));

    // Act & Assert
    // Trying to buy 100 shares, should throw an exception
    assertThrows(IllegalArgumentException.class, () -> {
      positionService.buy(ticker, 100, 10.0);
    });
  }
}