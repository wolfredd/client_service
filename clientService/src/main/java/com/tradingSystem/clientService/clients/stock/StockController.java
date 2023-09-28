package com.tradingSystem.clientService.clients.stock;

import com.tradingSystem.clientService.clients.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;


    @PostMapping("/createstock/{portfolioId}")
    public ResponseEntity<Client> createStock(
            @PathVariable("portfolioId") Integer portfolioId,
            @RequestParam(required = false) String stockname) {
        return ResponseEntity.ok(stockService.createStock(portfolioId, stockname));
    }

//    @DeleteMapping("/deletestock/{portfolioId}")
//    public ResponseEntity<Client> deleteStockByPortId(
//            @PathVariable("portfolioId") Integer portfolioId){
//        return ResponseEntity.ok(stockService.deleteStocksByPortId(portfolioId));
//    }

    @DeleteMapping("/deletestock/{portfolioId}")
    public ResponseEntity<Client> deleteStockByPortId(
            @PathVariable("portfolioId") Integer portfolioId,
            @RequestParam(required = false) Integer stockId) {
        return ResponseEntity.ok(stockService.deleteStocksById(portfolioId, stockId));
    }

    @GetMapping("/getstocks/{portfolioId}")
    public ResponseEntity<List> getStocks(
            @PathVariable("portfolioId") Integer portfolioId) {
        return ResponseEntity.ok(stockService.listOfStocksInPortfolio(portfolioId));
    }

    @GetMapping("/getstocksbyclientid/{clientId}")
    public ResponseEntity<List> getStocksByClientId(
            @PathVariable("clientId") Integer clientId) {
        return ResponseEntity.ok(stockService.listOfStockByUserId(clientId));
    }

    @PostMapping("/addtostock/{stockId}")
    public ResponseEntity<Stock> addToStock(
            @PathVariable("stockId") Integer stockId,
            @RequestParam(required = false) Integer quantity) {
        return ResponseEntity.ok(stockService.addToStock(stockId, quantity));
    }

//    @GetMapping("/changeport/{portfolioId}")
//    public ResponseEntity<String> changePort(
//            @PathVariable("portfolioId") Integer portfolioId){
//        return ResponseEntity.ok(stockService.changePortId(portfolioId));
//    }


    @GetMapping("/changeport/{portfolioId}")
    public ResponseEntity<String> changePort(
            @PathVariable("portfolioId") Integer portfolioId) {
        return ResponseEntity.ok(stockService.changePortId(portfolioId));
    }


}
