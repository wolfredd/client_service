package com.tradingSystem.clientService.clients.portfolio;


import com.tradingSystem.clientService.clients.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/createport/{portname}")
    public ResponseEntity<Client> createPortfolio(
            @PathVariable("portname") String portname) {
        return ResponseEntity.ok(portfolioService.createPortfolio(portname));
    }

    @DeleteMapping("/deleteport/{portid}")
    public ResponseEntity<Client> deletePortfolio(
            @PathVariable("portid") Integer portid) {

        //make it delete all stocks
        return ResponseEntity.ok(portfolioService.deletePortfolio(portid));

    }

    @GetMapping("/getports/{clientId}")
    public ResponseEntity<List> getPorts(
            @PathVariable("clientId") Integer clientId) {
        return ResponseEntity.ok(portfolioService.listOfPorts(clientId));
    }

    @GetMapping("/getports")
    public ResponseEntity<List> getPorts(){
        return ResponseEntity.ok(portfolioService.listOfPort());
    }

    @GetMapping("/getportsbyid/{clientId}")
    public ResponseEntity<List> getPortsById(
            @PathVariable("clientId") Integer clientId){
        return ResponseEntity.ok(portfolioService.listOfPortsByUserId(clientId));
    }


//    @PostMapping("/create")
//    public ResponseEntity<Portfolio> createPortfolio(){
//        return ResponseEntity.ok(portfolioService.createPortfolio());
//    }
//
//    @PostMapping("/hello")
//    public ResponseEntity<String> hello(){
//        return ResponseEntity.ok("hello");
//    }


}
