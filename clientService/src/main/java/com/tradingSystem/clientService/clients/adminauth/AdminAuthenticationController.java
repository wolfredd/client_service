package com.tradingSystem.clientService.clients.adminauth;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller
@RequestMapping("/api/v1/auth/admin")
@RequiredArgsConstructor
public class AdminAuthenticationController {

    private final AdminAuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AdminAuthenticationResponse> register(@RequestBody AdminRegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AdminAuthenticationResponse> authenticate(@RequestBody AdminAuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

}

