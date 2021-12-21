package com.kpparekh.InvestmentPortfolio.Controllers;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.kpparekh.InvestmentPortfolio.Repostiories.InvestmentRepo;
import com.kpparekh.InvestmentPortfolio.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.kpparekh.InvestmentPortfolio.service.IInvestmentService;

@RestController
public class InvestmentController {

    @Autowired
    InvestmentRepo investmentRepo;

    @GetMapping("/investments")
    @ResponseBody
    public ResponseEntity<List<UserInvestment>> allInvestments() {
        List<Investment> investments = (List<Investment>) investmentRepo.findAll();
        List<UserInvestment> userInvestments = new ArrayList<>();

        for (Investment investment: investments) {
            UserInvestment userInvestment = new UserInvestment(investment.getQuote(), investment.getQuantity(), investment.getPrice(), investment.getChange(), investment.getBuydate(), investment.getId());
            userInvestments.add(userInvestment);
        }

        return new ResponseEntity<List<UserInvestment>>(userInvestments, HttpStatus.OK);
    }

    @GetMapping("/investment/{quote}")
    @ResponseBody
    public ResponseEntity<UserInvestment> investment(@PathVariable String quote) {
        quote = quote.toUpperCase(Locale.ROOT);
        Investment investment = investmentRepo.findByQuote(quote);
        UserInvestment userInvestment = new UserInvestment(investment.getQuote(), investment.getQuantity(), investment.getPrice(), investment.getChange(), investment.getBuydate(), investment.getId());

        return new ResponseEntity<UserInvestment>(userInvestment, HttpStatus.OK);
    }

    @Autowired
    IInvestmentService investmentService;
    @GetMapping("/getQuotes")
    public List<String> getAllQuotes(){
        var quotes = (List<String>) investmentService.getQuotes();

        return quotes;
    }

    @PostMapping(value = "/addinvestment", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<String> addInvestment(@RequestBody AddInvestment investmentData, UriComponentsBuilder builder) throws IOException {

        String quote = investmentData.quote.toUpperCase(Locale.ROOT);
        double quantity = investmentData.quantity;
        double price  = 0;
        LocalDate currentDate = LocalDate.now();
        long userid = (long) (Math.random() * (10001) + 1);
        LocalDate buyDate = investmentData.buyDate;

        double change = 0;


        if (buyDate != null) {
            Config cfg = Config.builder()
                    .key("9VX7R0WNMRAF0HIG")
                    .timeOut(10)
                    .build();

            double firstPrice = 0;

            AlphaVantage.api().init(cfg);
            TimeSeriesResponse dataResponse = AlphaVantage.api()
                    .timeSeries()
                    .daily()
                    .adjusted()
                    .forSymbol(quote)
                    .outputSize(OutputSize.FULL)
                    .fetchSync();

            List<StockUnit> stockUnits = dataResponse.getStockUnits();
            price = stockUnits.get(0).getAdjustedClose();
            for (StockUnit stockUnit: stockUnits) {
                if (stockUnit.getDate().equals(buyDate.toString())) {
                    firstPrice = stockUnit.getAdjustedClose();
                }
            }

            if (firstPrice == -1) {
                change = 0;
            } else {
                change = Math.round((((price - firstPrice) / firstPrice) * 100) * 100.0) / 100.0;
            }
        } else {
            buyDate = LocalDate.now();
        }

        Investment investment = new Investment(quote, quantity, price, change, currentDate, buyDate, userid);
        investmentRepo.save(investment);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/addinvestment/{id}").buildAndExpand(investment.getId()).toUri());
        return new ResponseEntity<String>("Data Added", HttpStatus.OK);

    }

    @PostMapping(value = "/updateinvestment", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> updateInvestment(@RequestBody AddInvestment investmentData, UriComponentsBuilder builder) {
        Investment currentInvestment = investmentRepo.findByid(investmentData.id);
        currentInvestment.setQuantity(investmentData.quantity);
        currentInvestment.setBuydate(investmentData.buyDate);
        double firstPrice = 0;

        Config cfg = Config.builder()
                .key("9VX7R0WNMRAF0HIG")
                .timeOut(10)
                .build();

        AlphaVantage.api().init(cfg);
        LocalDate buyDate = investmentData.buyDate;

        String quote = currentInvestment.getQuote();
        double change = 0;
        double price = currentInvestment.getPrice();

        TimeSeriesResponse dataResponse = AlphaVantage.api()
                .timeSeries()
                .daily()
                .adjusted()
                .forSymbol(quote)
                .outputSize(OutputSize.FULL)
                .fetchSync();

        List<StockUnit> stockUnits = dataResponse.getStockUnits();
        for (StockUnit stockUnit: stockUnits) {
            if (stockUnit.getDate().equals(buyDate.toString())) {
                firstPrice = stockUnit.getAdjustedClose();
            }
        }

        if (firstPrice == -1) {
            change = 0;
        } else {
            change = Math.round((((price - firstPrice) / firstPrice) * 100) * 100.0) / 100.0;
        }

        currentInvestment.setChange(change);
        investmentRepo.save(currentInvestment);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteInvestment(@PathVariable String id) {
        Long edit_id = Long.parseLong(id);
        Investment currentInvestment = investmentRepo.findByid(edit_id);
        investmentRepo.delete(currentInvestment);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/quotes")
    public ResponseEntity<List<String>> quotes() {
        List<String> quotes = investmentRepo.findDistinctQuote();
        return new ResponseEntity<List<String>>(quotes, HttpStatus.OK);
    }

    @GetMapping("/allgraph")
    public ResponseEntity<List<Graph>> allGraphData() {
        Config cfg = Config.builder()
                .key("9VX7R0WNMRAF0HIG")
                .timeOut(10)
                .build();
        AlphaVantage.api().init(cfg);
        List<String> quotes = new ArrayList<>();
        quotes = investmentRepo.findDistinctQuote();
        List<Graph> data = new ArrayList<Graph>();

        List<String> dates = new ArrayList<String>();
        List<Double> price = new ArrayList<Double>(Collections.nCopies(100, 0.0));

        TimeSeriesResponse dataResponse = AlphaVantage.api()
                .timeSeries()
                .daily()
                .adjusted()
                .forSymbol(quotes.get(0))
                .outputSize(OutputSize.COMPACT)
                .fetchSync();
        List<StockUnit> stockUnits = dataResponse.getStockUnits();
        for (StockUnit dataresp: stockUnits) {
            dates.add(dataresp.getDate());
        }

        for (String quote: quotes) {
            TimeSeriesResponse quoteResp = AlphaVantage.api()
                    .timeSeries()
                    .daily()
                    .adjusted()
                    .forSymbol(quote)
                    .outputSize(OutputSize.COMPACT)
                    .fetchSync();
                List<StockUnit> quoteUnits = quoteResp.getStockUnits();

            double quantity = investmentRepo.findSumQuantity(quote);

            for (int i = 0; i < quoteUnits.size(); i++) {
                double closeVal = (quoteUnits.get(i).getAdjustedClose() * quantity);
                price.set(i, (double) Math.round((price.get(i) + closeVal) * 100.0 / 100.0));
            }

        }

        for (int i = dates.size() - 1; i >= 0; i--) {
            data.add(new Graph(dates.get(i), price.get(i), price.get(i)));
        }

        return new ResponseEntity<List<Graph>>(data, HttpStatus.OK);

    }

}