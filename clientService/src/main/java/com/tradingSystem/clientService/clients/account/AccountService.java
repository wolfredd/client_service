package com.tradingSystem.clientService.clients.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingSystem.clientService.clients.activity.Activity;
import com.tradingSystem.clientService.clients.client.Client;
import com.tradingSystem.clientService.clients.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Integer getCurrentBalance(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Account account = accountRepository.findById(client.getAccountId()).orElseThrow();

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " got their current balance of " + account.getAmount());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return account.getAmount();

    }

    public Integer topUpBalance(Integer amount){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Account account = accountRepository.findById(client.getAccountId()).orElseThrow();
        account.setAmount(account.getAmount() + amount);

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " topped up their current balance with " + amount);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        accountRepository.save(account);
        return account.getAmount();

    }

    public Integer deductFromBalance(Integer amount){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Account account = accountRepository.findById(client.getAccountId()).orElseThrow();

        if (account.getAmount() >= amount) {
            account.setAmount(account.getAmount() - amount);
            accountRepository.save(account);

            Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " deducted their current balance with " + account.getAmount());

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return account.getAmount();
        }
        else {
            return null;
        }



    }

}
