package com.tradingSystem.clientService.clients.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingSystem.clientService.clients.account.Account;
import com.tradingSystem.clientService.clients.account.AccountRepository;
import com.tradingSystem.clientService.clients.activity.Activity;
import com.tradingSystem.clientService.clients.client.Client;
import com.tradingSystem.clientService.clients.client.ClientRepository;
import com.tradingSystem.clientService.clients.client.Role;
import com.tradingSystem.clientService.clients.config.JwtService;
import com.tradingSystem.clientService.clients.portfolio.Portfolio;
import com.tradingSystem.clientService.clients.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PortfolioRepository portfolioRepository;
    private final AccountRepository accountRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public List getAllClients(){
        List<Client> clients =  clientRepository.findAllByRole(Role.CLIENT);
        System.out.println(clients);
        return clients;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        Optional<Client> clientOptional = clientRepository.findByEmail(request.getEmail());

        if(clientOptional.isEmpty()) {
            var user = Client.builder().firstName(request.getFirstname()).lastName(request.getLastname()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.CLIENT).build();
            clientRepository.save(user);
            var jwtToken = jwtService.generateToken(user);

            Portfolio defaultPortfolio = Portfolio.builder().portfolioName("Default").userId(user.getId()).build();
            portfolioRepository.save(defaultPortfolio);

            Account account = Account.builder().userId(user.getId()).amount(0).build();
            accountRepository.save(account);

            user.setAccountId(account.getId());
            clientRepository.save(user);
            user.setDefaultPortfolioId(defaultPortfolio.getId());
            clientRepository.save(user);

            Activity activity = new Activity(user.getId(), user.getFirstName() + " " + user.getLastName(),"Client "+ user.getId() + " signed up");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return AuthenticationResponse.builder().token(jwtToken).build();
        }
        else {
            return null;
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = clientRepository.findByEmail(request.getEmail()).orElseThrow();
        if (user.getRole() == Role.CLIENT) {
            var jwtToken = jwtService.generateToken(user);

            Activity activity = new Activity(user.getId(), user.getFirstName() + " " + user.getLastName(),"Client "+ user.getId() + " signed in");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return AuthenticationResponse.builder().token(jwtToken).build();
        }

        else {
            return null;
        }


    }
}
