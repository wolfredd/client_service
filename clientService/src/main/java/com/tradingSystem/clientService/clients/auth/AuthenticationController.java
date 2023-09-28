package com.tradingSystem.clientService.clients.auth;


import com.tradingSystem.clientService.clients.stock.Stock;
import com.tradingSystem.clientService.clients.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final StockService stockService;

    @GetMapping("/getallclients")
    public ResponseEntity<List> getAllClients(){
        return ResponseEntity.ok(service.getAllClients());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        AuthenticationResponse response =service.register(request);
        if (response == null) return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
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
        return ResponseEntity.ok(stockService.updateStock(stockId, quantity));
    }


}

