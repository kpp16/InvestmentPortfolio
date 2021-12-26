package com.kpparekh.InvestmentPortfolio.Controllers;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.StockUnit;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import com.kpparekh.InvestmentPortfolio.Repostiories.InvestmentRepo;
import com.kpparekh.InvestmentPortfolio.models.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;

import com.kpparekh.InvestmentPortfolio.service.IInvestmentService;

@RestController
public class InvestmentController {

    @GetMapping("/")
    public String index() {
        return "Hello, World";
    }

    @Autowired
    InvestmentRepo investmentRepo;

    @GetMapping("/investments")
    @ResponseBody
    public ResponseEntity<List<UserInvestment>> allInvestments() throws IOException, InterruptedException {
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

    @PostMapping(value = "/addinvestment", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<String> addInvestment(@RequestBody AddInvestment investmentData, UriComponentsBuilder builder) throws IOException, InterruptedException {

        String quote = investmentData.quote.toUpperCase(Locale.ROOT);
        double quantity = investmentData.quantity;
        double price  = 0;
        LocalDate currentDate = LocalDate.now();
        long userid = (long) (Math.random() * (10001) + 1);
        LocalDate buyDate = investmentData.buyDate;

        double change = 0;


        if (buyDate != null) {

            double firstPrice = 0;

            String url = "https://api.twelvedata.com/time_series?apikey=cf86792a7e7c40e6875feaa45820aafa&interval=1day&start_date=" + buyDate.toString() + "+21:37:00&symbol=" + quote;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            final JSONObject obj = new JSONObject(response.body());
            final JSONArray stockValues = obj.getJSONArray("values");
            final JSONObject stockNow = (JSONObject) stockValues.get(0);
            price = stockNow.getDouble("close");

            final JSONObject stockFirst = (JSONObject) stockValues.get(stockValues.length() - 1);
            firstPrice = stockFirst.getDouble("close");

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
    public ResponseEntity<Void> updateInvestment(@RequestBody AddInvestment investmentData, UriComponentsBuilder builder) throws IOException, InterruptedException {
        Investment currentInvestment = investmentRepo.findByid(investmentData.id);
        currentInvestment.setQuantity(investmentData.quantity);
        currentInvestment.setBuydate(investmentData.buyDate);
        double firstPrice = 0;
        LocalDate buyDate = investmentData.buyDate;

        String quote = currentInvestment.getQuote();
        double change = 0;
        double price = currentInvestment.getPrice();

        String url = "https://api.twelvedata.com/time_series?apikey=cf86792a7e7c40e6875feaa45820aafa&interval=1day&start_date=" + buyDate.toString() + "+21:37:00&symbol=" + quote;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());


        final JSONObject obj = new JSONObject(response.body());
        final JSONArray stockValues = obj.getJSONArray("values");
        final JSONObject stockFirst = (JSONObject) stockValues.get(stockValues.length() - 1);
        firstPrice = stockFirst.getDouble("close");

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
    public ResponseEntity<List<Graph>> allGraphData() throws IOException, InterruptedException {
        List<String> quotes = new ArrayList<>();
        quotes = investmentRepo.findDistinctQuote();
        List<Graph> data = new ArrayList<Graph>();

        List<String> dates = new ArrayList<String>(Collections.nCopies(100, ""));
        List<Double> price = new ArrayList<Double>(Collections.nCopies(100, 0.0));

        int size = 88;

        for (String quote: quotes) {

            String url = "https://api.twelvedata.com/time_series?apikey=cf86792a7e7c40e6875feaa45820aafa&interval=1day&format=JSON&previous_close=true&symbol=" + quote + "&outputsize=100";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            final JSONObject obj = new JSONObject(response.body());
            final JSONArray stockValues = obj.getJSONArray("values");
            size = stockValues.length();
            for (int i = stockValues.length() - 1; i >= 0; i--) {
                final JSONObject stockOBJ = stockValues.getJSONObject(i);
                dates.set(stockValues.length() - 1 - i, stockOBJ.getString("datetime"));
                price.set(stockValues.length() - 1 - i, price.get(stockValues.length() - 1 - i) + stockOBJ.getDouble("close"));
            }
        }

        for (int i = 0; i < size; i++) {
            data.add(new Graph(dates.get(i), Math.round(price.get(i) * 100.0) / 100.0, Math.round(price.get(i) * 100.0) / 100.0));
        }

        return new ResponseEntity<List<Graph>>(data, HttpStatus.OK);

    }
}