package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.data.utils.QuoteHttpHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Enable Mockito
@ExtendWith(MockitoExtension.class)
public class QuoteServiceTest {

  // 1. Define the dependencies we want to mock (stubs)
  @Mock
  private QuoteDao quoteDao;

  @Mock
  private QuoteHttpHelper httpHelper;

  // 2. Define the service we want to test, and automatically inject the mocks
  @InjectMocks
  private QuoteService quoteService;

  @Test
  public void fetchQuoteDataFromAPI_Success() {
    // --- Arrange (Prepare the scenario) ---
    String ticker = "MSFT";
    Quote mockQuote = new Quote();
    mockQuote.setTicker(ticker);
    mockQuote.setPrice(100.0);

    // Tell the httpHelper mock: when fetchQuoteInfo("MSFT") is called, return mockQuote
    when(httpHelper.fetchQuoteInfo(ticker)).thenReturn(mockQuote);

    // Tell the dao mock: when save is called, return mockQuote
    when(quoteDao.save(any(Quote.class))).thenReturn(mockQuote);

    // --- Act (Execute) ---
    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    // --- Assert (Verify) ---
    assertTrue(result.isPresent());
    assertEquals(100.0, result.get().getPrice());

    // Verify that dao.save was actually called
    verify(quoteDao, times(1)).save(mockQuote);
  }

  @Test
  public void fetchQuoteDataFromAPI_Failure() {
    // --- Arrange ---
    String ticker = "INVALID";
    // Tell the helper: this time throw an exception
    when(httpHelper.fetchQuoteInfo(ticker)).thenThrow(new IllegalArgumentException("Not Found"));

    // --- Act ---
    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(ticker);

    // --- Assert ---
    // assertTrue(result.isEmpty());
    assertFalse(result.isPresent());  // This checks that the result is empty (i.e., no value present)
  }
}
