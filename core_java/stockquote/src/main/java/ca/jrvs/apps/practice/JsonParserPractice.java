package ca.jrvs.apps.practice;

import ca.jrvs.apps.practice.dto.Company;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserPractice {

  //
  public static final String companyStr = "{\n"
      + "   \"symbol\":\"AAPL\",\n"
      + "   \"companyName\":\"Apple Inc.\",\n"
      + "   \"exchange\":\"Nasdaq Global Select\",\n"
      + "   \"description\":\"Apple Inc is designs, manufactures and markets mobile communication and media devices and personal computers, and sells a variety of related software, services, accessories, networking solutions and third-party digital content and applications.\",\n"
      + "   \"CEO\":\"Timothy D. Cook\",\n"
      + "   \"sector\":\"Technology\",\n"
      + "   \"financials\":[\n"
      + "      {\n"
      + "         \"reportDate\":\"2018-12-31\",\n"
      + "         \"grossProfit\":32031000000,\n"
      + "         \"costOfRevenue\":52279000000,\n"
      + "         \"operatingRevenue\":84310000000,\n"
      + "         \"totalRevenue\":84310000000,\n"
      + "         \"operatingIncome\":23346000000,\n"
      + "         \"netIncome\":19965000000\n"
      + "      },\n"
      + "      {\n"
      + "         \"reportDate\":\"2018-09-30\",\n"
      + "         \"grossProfit\":24084000000,\n"
      + "         \"costOfRevenue\":38816000000,\n"
      + "         \"operatingRevenue\":62900000000,\n"
      + "         \"totalRevenue\":62900000000,\n"
      + "         \"operatingIncome\":16118000000,\n"
      + "         \"netIncome\":14125000000\n"
      + "      }\n"
      + "   ],\n"
      + "   \"dividends\":[\n"
      + "      {\n"
      + "         \"exDate\":\"2018-02-09\",\n"
      + "         \"paymentDate\":\"2018-02-15\",\n"
      + "         \"recordDate\":\"2018-02-12\",\n"
      + "         \"declaredDate\":\"2018-02-01\",\n"
      + "         \"amount\":0.63\n"
      + "      },\n"
      + "      {\n"
      + "         \"exDate\":\"2017-11-10\",\n"
      + "         \"paymentDate\":\"2017-11-16\",\n"
      + "         \"recordDate\":\"2017-11-13\",\n"
      + "         \"declaredDate\":\"2017-11-02\",\n"
      + "         \"amount\":0.63\n"
      + "      }\n"
      + "   ]\n"
      + "}";

  public static void main(String[] args) {
    // 1. ObjectMapper
    ObjectMapper mapper = new ObjectMapper();

    try {
      // 2. JSON (Deserialization)
      // readValue
      Company company = mapper.readValue(companyStr, Company.class);

      // 3.
      System.out.println("JSON Parsing Success!");
      System.out.println("Symbol: " + company.getSymbol());
      System.out.println("CEO: " + company.getCeo());
      System.out.println("Sector: " + company.getSector());

      //
      System.out.println("Latest Dividend Amount: " + company.getDividends().get(0).getAmount());
      System.out.println("Latest Report Gross Profit: " + company.getFinancials().get(0).getGrossProfit());

      System.out.println("Type of financials: " + company.getFinancials().getClass().getName());
      System.out.println("Type of dividends: " + company.getDividends().getClass().getName());
    } catch (JsonProcessingException e) {
      //
      e.printStackTrace();
    }
  }
}