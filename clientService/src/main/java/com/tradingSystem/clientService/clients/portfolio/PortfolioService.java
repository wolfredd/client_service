package com.tradingSystem.clientService.clients.portfolio;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingSystem.clientService.clients.activity.Activity;
import com.tradingSystem.clientService.clients.client.Client;
import com.tradingSystem.clientService.clients.client.ClientRepository;
import com.tradingSystem.clientService.clients.stock.Stock;
import com.tradingSystem.clientService.clients.stock.StockRepository;
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
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;



    public Client createPortfolio(String portfolioName){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Portfolio portfolio = new Portfolio(portfolioName, client.getId());
        portfolioRepository.save(portfolio);

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " created portfolio " + portfolio.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return client;
    }


    public Client deletePortfolio(Integer portfolioId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();

        if (Objects.equals(portfolio.getUserId(), client.getId())) {
            //stockRepository.deleteAllByPortfolioId(portfolioId);
            stockRepository.moveStocksToDefault(client.getDefaultPortfolioId(), portfolioId);
            portfolioRepository.deleteById(portfolioId);
        }

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " deleted portfolio " + portfolio.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return client;


    }

    public List listOfPorts(Integer clientId){

        List<Portfolio> listOfPortfolios = portfolioRepository.findAllByUserId(clientId);

        return listOfPortfolios;

    }


    public List listOfPort(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        List<Portfolio> listOfPortfolios = portfolioRepository.findAllByUserId(client.getId());

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " requested for all their portfolios ");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return listOfPortfolios;

    }


    public List listOfPortsByUserId(Integer clientId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        List<Portfolio> listOfPortfolios = portfolioRepository.findAllByUserId(clientId);

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ clientId + " requested for all their portfolios ");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return listOfPortfolios;

    }



//    public Portfolio createPortfolio(){
//        Portfolio portfolio = new Portfolio();
//        portfolioRepository.save(portfolio);
//        return portfolio;
//
//    }
}
