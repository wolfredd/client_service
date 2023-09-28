package com.tradingSystem.clientService.clients.account;

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
@Table(name ="account")
public class Account {

    @Id
    @GeneratedValue//(strategy = GenerationType.SEQUENCE)
    private Integer Id;
    @Column(nullable = true)
    private Integer amount = 0;
    @Column(nullable = true)
    private Integer userId;


    public Account(Integer userId) {
        this.userId = userId;
        this.amount = 0;
    }

}
