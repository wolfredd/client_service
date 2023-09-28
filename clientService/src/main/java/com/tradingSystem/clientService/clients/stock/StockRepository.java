package com.tradingSystem.clientService.clients.stock;

import com.tradingSystem.clientService.clients.client.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer>{

    Optional<Stock> deleteAllByPortfolioId(Integer portfolioId);
    List<Stock> findAllByPortfolioId(Integer portfolioId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Stock stock set stock.portfolioId = :defaultportfolioid where stock.portfolioId = :selectedportfolioid")
    void moveStocksToDefault (@Param("defaultportfolioid") Integer defaultPortfolioId, @Param("selectedportfolioid") Integer selectedPortfolioId);

}