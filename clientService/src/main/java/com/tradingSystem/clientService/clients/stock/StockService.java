package com.tradingSystem.clientService.clients.stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingSystem.clientService.clients.activity.Activity;
import com.tradingSystem.clientService.clients.client.Client;
import com.tradingSystem.clientService.clients.client.ClientRepository;
import com.tradingSystem.clientService.clients.portfolio.Portfolio;
import com.tradingSystem.clientService.clients.portfolio.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public Client createStock(Integer portfolioId, String stockName) {
        List<String> stocks = List.of(
                "AAPL",
                "IBM",
                "NFLX",
                "GOOGL",
                "TSLA",
                "ORCL",
                "AMZN",
                "MSFT"
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();

        if (Objects.equals(portfolio.getUserId(), client.getId())) {
            if (stocks.contains(stockName)) {
                Stock stock = new Stock(portfolioId, stockName);
                stockRepository.save(stock);

                Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " created a new stock " + stock.getId());

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String serializedActivity =objectMapper.writeValueAsString(activity);
                    rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                    System.out.println(serializedActivity);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("Invalid stock");
            }
        }

        return client;

    }

//    public Client deleteStocksByPortId(Integer portfolioId){
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//
//        Client client = clientRepository.findByEmail(userEmail).orElseThrow();
//
//        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();
//
//        if (Objects.equals(portfolio.getUserId(), client.getId())) {
//            stockRepository.deleteById(portfolioId);
//            //portfolioRepository.deleteById(portfolioId);
//        }
//
//        return client;
//    }


    public Client deleteStocksById(Integer portfolioId, Integer stockId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();

        if (Objects.equals(portfolio.getUserId(), client.getId())) {
            stockRepository.deleteById(stockId);
            //portfolioRepository.deleteById(portfolioId);

            Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " deleted stock " + stockId);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return client;
    }


    public List listOfStocksInPortfolio(Integer portfolioId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        List <Stock> listOfStocks = stockRepository.findAllByPortfolioId(portfolioId);

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " requested for a list of all their stocks in portfolio " + portfolioId);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return listOfStocks;

    }

    public List listOfStockByUserId(Integer clientId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        List<Portfolio> listOfPortfolios = portfolioRepository.findAllByUserId(clientId);

        List <Stock> listOfStocks = new ArrayList<>();

        for (Portfolio portfolio:listOfPortfolios) {
            listOfStocks.addAll(stockRepository.findAllByPortfolioId(portfolio.getId()));
        }

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ clientId + " requested for a list of all their stocks ");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return listOfStocks;

    }

    public String changePortId(Integer portfolioId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Portfolio portfolio = portfolioRepository.findById(portfolioId).orElseThrow();

        if (Objects.equals(portfolio.getUserId(), client.getId())) {
            //stockRepository.deleteById(stockId);
            //portfolioRepository.deleteById(portfolioId);

            stockRepository.moveStocksToDefault(client.getDefaultPortfolioId(), portfolioId);

            Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " moved their stocks to their default portfolio ");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return "job done";
        }

        return "failed";

//        stockRepository.moveStocksToDefault(1, portfolioId);
//        return "success";

    }

    public Stock addToStock(Integer stockId, Integer quantity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Stock stock = stockRepository.findById(stockId).orElseThrow();

        stock.setQuantity(stock.getQuantity() + quantity);

        stockRepository.save(stock);

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " added quantity of " + quantity + " to stock " + stockId);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return stock;
    }

    public Stock updateStock(Integer stockId, Integer quantity){
        Stock stock = stockRepository.findById(stockId).orElseThrow();
        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);
        Optional<Portfolio> portfolioOptional = portfolioRepository.findById(stock.getPortfolioId());

        Integer clientId = portfolioOptional.map(Portfolio::getUserId).orElse(null);
        Activity activity = new Activity("Client "+ clientId + " added quantity of " + quantity + " to stock " + stockId);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return stock;
    }

    public Stock removeFromStock(Integer stockId, Integer quantity) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Stock stock = stockRepository.findById(stockId).orElseThrow();

        if (stock.getQuantity() >= quantity) {
            stock.setQuantity(stock.getQuantity() - quantity);
            stockRepository.save(stock);

            Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " removed quantity of " + quantity + " from stock " + stockId);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return stock;
        } else {
            return null;
        }

    }
}
