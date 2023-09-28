package com.tradingSystem.clientService.clients.portfolio;

import com.tradingSystem.clientService.clients.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

    List<Portfolio> findAllByUserId(Integer userId);

}
