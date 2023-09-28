package com.tradingSystem.clientService.clients.portfolio;

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
@Table(name ="portfolio")
public class Portfolio {

    @jakarta.persistence.Id
    @GeneratedValue//(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    private Integer Id;
    @Column(nullable = true)
    private String portfolioName;
    @Column(nullable = true)
    private Integer userId;


    public Portfolio(String portfolioName, Integer userId) {
        this.portfolioName = portfolioName;
        this.userId = userId;
    }
}
