package com.kpparekh.InvestmentPortfolio.service;

import com.kpparekh.InvestmentPortfolio.Repostiories.InvestmentRepo;
import com.kpparekh.InvestmentPortfolio.service.IInvestmentService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestmentService implements IInvestmentService {

    @Autowired
    private InvestmentRepo repository;

    @Override
    public List<String> getQuotes() {
        return null;
    }

//    @Override
//    public List<String> getQuotes() {
//
//        var quotes = (List<String>) repository.getQuotes();
//        return quotes;
//    }
}