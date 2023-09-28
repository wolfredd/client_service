package com.tradingSystem.clientService.clients.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingSystem.clientService.clients.activity.Activity;
import com.tradingSystem.clientService.clients.config.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public Client getCurrentClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        //User user = .orElseThrow();
        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " got the current client logged in ");

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


    @Transactional
    public Client updateClientEmail(String email){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        if(email!=null){
            client.setEmail(email);
        }

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " changed their email");

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


    @Transactional
    public Client updateClientFirstName(String firstName){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        if(firstName!=null){
            client.setFirstName(firstName);
        }

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " changed their firstname");

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


    @Transactional
    public Client updateClientLastName(String lastName){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client client = clientRepository.findByEmail(userEmail).orElseThrow();

        if(lastName!=null){
            client.setLastName(lastName);
        }

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ client.getId() + " changed their lastname");

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



























    public boolean isUserValidated(@RequestHeader(value = "Authorization")String authorizationHeader){
        final String jwt;
        final String userEmail;
        UserDetails userDetails = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usersEmail = authentication.getName();

        Client client = clientRepository.findByEmail(usersEmail).orElseThrow();

        jwt = authorizationHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        userDetails = this.userDetailsService.loadUserByUsername(userEmail);

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ userEmail + " checked if they were validated");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return  jwtService.isTokenValid(jwt, userDetails);


    }

    public boolean isUserValidatedByEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
        final String jwt;
        final String userEmail;
        UserDetails userDetails = null;
        jwt = authorizationHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usersEmail = authentication.getName();

        Client client = clientRepository.findByEmail(usersEmail).orElseThrow();

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ userEmail + " checked if they were validated by their email");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return  jwtService.isTokenValidByEmail(jwt, userEmail);

    }

    public String returnEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
        final String jwt;
        final String userEmail;
        UserDetails userDetails = null;
        jwt = authorizationHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usersEmail = authentication.getName();

        Client client = clientRepository.findByEmail(usersEmail).orElseThrow();

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ userEmail + " asked for their email");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return userEmail;
    }

    public String returnEmailFromJwt(@RequestHeader(value = "Authorization")String authorizationHeader){
        final String jwt;
        jwt = authorizationHeader.substring(7);

        String clientEmail = jwtService.extractUsername(jwt);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usersEmail = authentication.getName();

        Client client = clientRepository.findByEmail(usersEmail).orElseThrow();

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ clientEmail + " asked for their email through their jwt");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return clientEmail;
    }

    public String getHeader(@RequestHeader(value = "Authorization")String authorizationHeader){
        String header = authorizationHeader;
        return header;
    }

    public boolean isUserInDatabase(@RequestHeader(value = "Authorization")String authorizationHeader){


        final String jwt;
        jwt = authorizationHeader.substring(7);

        String email = jwtService.extractUsername(jwt);


        String newEmail = String.valueOf(clientRepository.findByEmail(email));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usersEmail = authentication.getName();

        Client client = clientRepository.findByEmail(usersEmail).orElseThrow();

        Activity activity = new Activity(client.getId(), client.getFirstName() + " " + client.getLastName(),"Client "+ email + " checked if their email was in the database");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return newEmail != null;
    }
























    @Transactional
    public void updateClient(Integer Id, String email){
        Client client = clientRepository.findById(Id).orElseThrow(() -> new IllegalStateException("User with id: " + Id + " does not exist"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client clients = clientRepository.findByEmail(userEmail).orElseThrow();

        Activity activity = new Activity("Client "+ clients.getId() + " updated their email");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedActivity =objectMapper.writeValueAsString(activity);
            rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
            System.out.println(serializedActivity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


//        if(name != null && name.length() > 0 && !Objects.equals(student.getName(), name)){
//            student.setName(name);
//        }
//        if(email != null && email.length() > 0 && !Objects.equals(student.getEmail(), email)){
//            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
//            if(studentOptional.isPresent()){
//                throw new IllegalStateException("email taken");
//            }
//            student.setName(email);
//        }
        client.setEmail(email);
    }


//    public String createPortfolio(@RequestHeader(value = "Authorization")String authorizationHeader){
//
//        final String jwt;
//        jwt = authorizationHeader.substring(7);
//
//        String email = jwtService.extractUsername(jwt);
//
//        User user = userRepository.findUserByEmailIs(email);
//        Portfolio portfolio = new Portfolio();
//        user.listOfPortfolios.add(portfolio);
//
//        return "Portfolio Created";
//
//    }

}
