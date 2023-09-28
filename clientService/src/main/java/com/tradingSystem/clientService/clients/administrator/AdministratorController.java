package com.tradingSystem.clientService.clients.administrator;

import com.tradingSystem.clientService.clients.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;




    @GetMapping("/hi")
    public ResponseEntity<String> hi(){
        return ResponseEntity.ok("hi");
    }

    @GetMapping("/getallclients")
    public ResponseEntity<List> getAllClients(){
        return ResponseEntity.ok(administratorService.getAllClients());
    }

    @GetMapping("/currentAdministrator")
    public ResponseEntity<Client> returnCurrentAdministrator(){
        return ResponseEntity.ok(administratorService.getCurrentAdministrator());
    }


    @PutMapping("/updateAdministratorEmail/{email}")
    public ResponseEntity<Client> updateAdministratorEmail(
            @PathVariable("email") String email){
        return ResponseEntity.ok(administratorService.updateAdministratorEmail(email));
    }

    @PutMapping("/updateAdministratorFirstName/{firstName}")
    public ResponseEntity<Client> updateAdministratorFirstName(
            @PathVariable("firstName") String firstName){
        return ResponseEntity.ok(administratorService.updateAdministratorFirstName(firstName));
    }


    @PutMapping("/updateAdministratorLastName/{lastName}")
    public ResponseEntity<Client> updateAdministratorLastName(
            @PathVariable("lastName") String lastName){
        return ResponseEntity.ok(administratorService.updateAdministratorLastName(lastName));
    }








//    @GetMapping("/register")
//    public ResponseEntity<Boolean> isAdministratorValidated(@RequestHeader(value = "Authorization")String authorizationHeader){
//        return ResponseEntity.ok(administratorService.isUserValidated(administratorService.getHeader(authorizationHeader)));
//    }
//
//    @GetMapping("/register/email")
//    public ResponseEntity<Boolean> isAdministratorValidatedByEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
//        return ResponseEntity.ok(administratorService.isUserValidatedByEmail(administratorService.getHeader(authorizationHeader)));
//    }
//
//    @GetMapping("/register/header")
//    public ResponseEntity<Map<String, String>> returnHeader(@RequestHeader(value = "Authorization")String authorizationHeader){
//
//        Map<String, String> returnValue = new HashMap<>();
//        returnValue.put("Authorization", authorizationHeader);
//        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
//    }
//
//    @GetMapping("/register/returnemail")
//    public ResponseEntity<Map<String, String>> returnEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
//        Map<String, String> returnValue = new HashMap<>();
//        returnValue.put("email", administratorService.returnEmail(administratorService.getHeader(authorizationHeader)));
//        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
//
//    }
//
//    @GetMapping("/register/returnjwtemail")
//    public ResponseEntity<Map<String, String>> returnjwtEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
//        Map<String, String> returnValue = new HashMap<>();
//        returnValue.put("email", administratorService.returnEmailFromJwt(administratorService.getHeader(authorizationHeader)));
//        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
//
//    }
//
//    @GetMapping("/register/indatabase")
//    public ResponseEntity<Boolean> isUserInDatabase(@RequestHeader(value = "Authorization")String authorizationHeader){
//        return ResponseEntity.ok(administratorService.isUserInDatabase(administratorService.getHeader(authorizationHeader)));
//    }
//
//    @PutMapping("/update/{Id}")
//    public void updateClient(
//            @PathVariable("Id") Integer Id,
//            @RequestParam(required = false) String email) {
//        administratorService.updateClient(Id, email);
//    }

//    @PostMapping("/register/createportfolio")
//    public ResponseEntity<String> createPortfolio(@RequestHeader(value = "Authorization")String authorizationHeader){
//        return ResponseEntity.ok(userService.createPortfolio(userService.getHeader(authorizationHeader)));
//    }


}
