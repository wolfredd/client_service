package com.tradingSystem.clientService.clients.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;



    @GetMapping("/currentClient")
    public ResponseEntity<Client> returnCurrentUser(){
        return ResponseEntity.ok(clientService.getCurrentClient());
    }


    @PutMapping("/updateClientEmail/{email}")
    public ResponseEntity<Client> updateClientEmail(
            @PathVariable("email") String email){
        return ResponseEntity.ok(clientService.updateClientEmail(email));
    }

    @PutMapping("/updateClientFirstName/{firstName}")
    public ResponseEntity<Client> updateClientFirstName(
            @PathVariable("firstName") String firstName){
        return ResponseEntity.ok(clientService.updateClientFirstName(firstName));
    }


    @PutMapping("/updateClientLastName/{lastName}")
    public ResponseEntity<Client> updateClientLastName(
            @PathVariable("lastName") String lastName){
        return ResponseEntity.ok(clientService.updateClientLastName(lastName));
    }

    @GetMapping("/register")
    public ResponseEntity<Boolean> isUserValidated(@RequestHeader(value = "Authorization")String authorizationHeader){
        return ResponseEntity.ok(clientService.isUserValidated(clientService.getHeader(authorizationHeader)));
    }

    @GetMapping("/register/email")
    public ResponseEntity<Boolean> isUserValidatedByEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
        return ResponseEntity.ok(clientService.isUserValidatedByEmail(clientService.getHeader(authorizationHeader)));
    }

    @GetMapping("/register/header")
    public ResponseEntity<Map<String, String>> returnHeader(@RequestHeader(value = "Authorization")String authorizationHeader){

        Map<String, String> returnValue = new HashMap<>();
        returnValue.put("Authorization", authorizationHeader);
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @GetMapping("/register/returnemail")
    public ResponseEntity<Map<String, String>> returnEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
        Map<String, String> returnValue = new HashMap<>();
        returnValue.put("email", clientService.returnEmail(clientService.getHeader(authorizationHeader)));
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }

    @GetMapping("/register/returnjwtemail")
    public ResponseEntity<Map<String, String>> returnjwtEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
        Map<String, String> returnValue = new HashMap<>();
        returnValue.put("email", clientService.returnEmailFromJwt(clientService.getHeader(authorizationHeader)));
        return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }

    @GetMapping("/register/indatabase")
    public ResponseEntity<Boolean> isUserInDatabase(@RequestHeader(value = "Authorization")String authorizationHeader){
        return ResponseEntity.ok(clientService.isUserInDatabase(clientService.getHeader(authorizationHeader)));
    }

    @PutMapping("/update/{Id}")
    public void updateClient(
            @PathVariable("Id") Integer Id,
            @RequestParam(required = false) String email) {
        clientService.updateClient(Id, email);
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyClientToken() {
        return ResponseEntity.ok(true);
    }

//    @PostMapping("/register/createportfolio")
//    public ResponseEntity<String> createPortfolio(@RequestHeader(value = "Authorization")String authorizationHeader){
//        return ResponseEntity.ok(userService.createPortfolio(userService.getHeader(authorizationHeader)));
//    }


}
