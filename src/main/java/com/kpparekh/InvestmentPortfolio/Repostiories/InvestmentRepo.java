package com.kpparekh.InvestmentPortfolio.Repostiories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kpparekh.InvestmentPortfolio.models.Investment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface InvestmentRepo extends JpaRepository<Investment, Long> {
    Investment findByQuote(String quote);
    Investment findByid(Long id);

    @Query("select distinct i.quote from Investment i")
    List<String> findDistinctQuote();

    @Query("select SUM(i.quantity) from Investment i where i.quote LIKE %:quote%")
    double findSumQuantity(@Param("quote") String quote);

}
