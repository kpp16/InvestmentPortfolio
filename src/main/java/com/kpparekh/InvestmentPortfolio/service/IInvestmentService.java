package com.kpparekh.InvestmentPortfolio.service;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IInvestmentService {
    @Query("select distinct quote from investments i")
    List<String> getQuotes();
}
