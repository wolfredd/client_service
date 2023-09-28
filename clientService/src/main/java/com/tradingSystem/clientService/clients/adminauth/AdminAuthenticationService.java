package com.tradingSystem.clientService.clients.adminauth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingSystem.clientService.clients.activity.Activity;
import com.tradingSystem.clientService.clients.administrator.Administrator;
import com.tradingSystem.clientService.clients.administrator.AdministratorRepository;
import com.tradingSystem.clientService.clients.client.Client;
import com.tradingSystem.clientService.clients.client.ClientRepository;
import com.tradingSystem.clientService.clients.client.Role;
import com.tradingSystem.clientService.clients.config.JwtService;
import com.tradingSystem.clientService.clients.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminAuthenticationService {

    private final AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PortfolioRepository portfolioRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public AdminAuthenticationResponse register(AdminRegisterRequest request) {
        Optional<Client> clientOptional = clientRepository.findByEmail(request.getEmail());

        if(clientOptional.isEmpty()) {
            Client admin = Client.builder().firstName(request.getFirstname()).lastName(request.getLastname()).email(request.getEmail()).role(Role.ADMIN).password(passwordEncoder.encode(request.getPassword())).build();
            clientRepository.save(admin);
            var jwtToken = jwtService.generateToken(admin);

            Activity activity = new Activity(admin.getId(), admin.getFirstName() + " " + admin.getLastName(),"Admin "+ admin.getId() + " signed up");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return AdminAuthenticationResponse.builder().token(jwtToken).build();
        }
        else {
            return null;
        }





    }

//    public String register(RegisterRequest request) {
//        Optional<Client> clientOptional = repository.findByEmail(request.getEmail());
//        if(clientOptional.isEmpty()) {
//            var user = Client.builder()
//                    .firstName(request.getFirstName())
//                    .lastName(request.getLastName())
//                    .email(request.getEmail())
//                    .password(passwordEncoder.encode(request.getPassword()))
//                    .role(Role.USER)
//                    .build();
//            repository.save(user);
//            return "YOU HAVE SUCCESSFULLY REGISTERED WITH R2M ";
//        }
//        return "THE EMAIL YOU ARE REGISTERING WITH IS ALREADY ASSOCIATED WITH AN ACCOUNT"; // I have to refactor to return the right status code if email already exists.
//    }


    public AdminAuthenticationResponse authenticate(AdminAuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var admin = clientRepository.findByEmail(request.getEmail()).orElseThrow();
        if (admin.getRole() == Role.ADMIN) {
            var jwtToken = jwtService.generateToken(admin);

            Activity activity = new Activity(admin.getId(), admin.getFirstName() + " " + admin.getLastName(),"Admin "+ admin.getId() + " signed in");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return AdminAuthenticationResponse.builder().token(jwtToken).build();
        }
        else {
            return null;
        }

    }
}
