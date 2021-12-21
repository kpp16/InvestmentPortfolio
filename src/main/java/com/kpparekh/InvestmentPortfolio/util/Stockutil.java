package com.kpparekh.InvestmentPortfolio.util;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.AlphaVantageException;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class Stockutil {

    public double closeValue;

    public Stockutil(double closeValue) {
        this.closeValue = closeValue;
    }

    public double getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(double closeValue) {
        this.closeValue = closeValue;
    }

//    public double getHistoricalData(String quote, LocalDate startDate) {
//        try {
//            getStockDataAlphaVantageAPI(quote, startDate);
//            return this.closeValue;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }

}