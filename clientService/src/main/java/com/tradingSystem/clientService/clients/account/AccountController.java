package com.tradingSystem.clientService.clients.account;

import com.tradingSystem.clientService.clients.client.Client;
import com.tradingSystem.clientService.clients.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ClientService clientService;

    @GetMapping("/getcurrentbalance")
    public ResponseEntity<Integer> getCurrentBalance(){
        return ResponseEntity.ok(accountService.getCurrentBalance());
    }

    @PostMapping("/topup/{amount}")
    public ResponseEntity<Integer> topUpBalance(@PathVariable("amount") Integer amount){
        return ResponseEntity.ok(accountService.topUpBalance(amount));
    }

    @PostMapping("/deduct/{amount}")
    public ResponseEntity<Integer> deductBalance(@PathVariable("amount") Integer amount){
        return ResponseEntity.ok(accountService.deductFromBalance(amount));
    }

}
