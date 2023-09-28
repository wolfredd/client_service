package com.tradingSystem.clientService.clients.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="stock")
public class Stock {

    @Id
    @GeneratedValue//(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    private Integer Id;
    @Column(nullable = true)
    private String stockName;
    @Column(nullable = true)
    private Integer portfolioId;
    @Column(nullable = true)
    private Integer quantity = 0 ;


    public Stock(Integer portfolioId, String stockName){
        this.portfolioId = portfolioId;
        this.stockName = stockName;
    }
}