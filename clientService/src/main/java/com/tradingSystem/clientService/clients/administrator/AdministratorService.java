package com.tradingSystem.clientService.clients.administrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradingSystem.clientService.clients.activity.Activity;
import com.tradingSystem.clientService.clients.client.Client;
import com.tradingSystem.clientService.clients.client.ClientRepository;
import com.tradingSystem.clientService.clients.client.Role;
import com.tradingSystem.clientService.clients.config.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdministratorService {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Autowired
    private AdministratorRepository administratorRepository;
    private final ClientRepository clientRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public Client getCurrentAdministrator(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        //User user = .orElseThrow();
        Client administrator = clientRepository.findByEmail(userEmail).orElseThrow();

        if (administrator.getRole() == Role.ADMIN){
            Activity activity = new Activity(administrator.getId(), administrator.getFirstName() + " " + administrator.getLastName(),"Admin: "+ administrator.getId() + " returned curent administrator");

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String serializedActivity =objectMapper.writeValueAsString(activity);
                rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                System.out.println(serializedActivity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return administrator;
        }
        else {
            return null;
        }

    }

    public List getAllClients(){
        return clientRepository.findAllByRole(Role.CLIENT);
    }


    @Transactional
    public Client updateAdministratorEmail(String email){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client administrator = clientRepository.findByEmail(userEmail).orElseThrow();

        if(email!=null){
            if (administrator.getRole() == Role.ADMIN) {
                Activity activity = new Activity(administrator.getId(), administrator.getFirstName() + " " + administrator.getLastName(),"Admin " + administrator.getId() + " returned curent administrator");

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String serializedActivity = objectMapper.writeValueAsString(activity);
                    rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                    System.out.println(serializedActivity);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                administrator.setEmail(email);
            }
            else {
                return null;
            }
        }
        return administrator;
    }


    @Transactional
    public Client updateAdministratorFirstName(String firstName){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client administrator = clientRepository.findByEmail(userEmail).orElseThrow();

        if(firstName!=null){
            if (administrator.getRole() == Role.ADMIN) {
                Activity activity = new Activity("Admin " + administrator.getId() + " returned curent administrator");

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String serializedActivity = objectMapper.writeValueAsString(activity);
                    rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                    System.out.println(serializedActivity);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                administrator.setFirstName(firstName);
            }else {
                return null;
            }
        }
        return administrator;
    }


    @Transactional
    public Client updateAdministratorLastName(String lastName){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Client administrator = clientRepository.findByEmail(userEmail).orElseThrow();

        if(lastName!=null){
            if (administrator.getRole() == Role.ADMIN) {
                Activity activity = new Activity(administrator.getId(), administrator.getFirstName() + " " + administrator.getLastName(),"Admin " + administrator.getId() + " returned curent administrator");

                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String serializedActivity = objectMapper.writeValueAsString(activity);
                    rabbitTemplate.convertAndSend("activities.queue", serializedActivity);
                    System.out.println(serializedActivity);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                administrator.setLastName(lastName);
            }
            else {
                return null;
            }
        }
        return administrator;
    }



























//    public boolean isUserValidated(@RequestHeader(value = "Authorization")String authorizationHeader){
//        final String jwt;
//        final String userEmail;
//        UserDetails userDetails = null;
//
//        jwt = authorizationHeader.substring(7);
//        userEmail = jwtService.extractUsername(jwt);
//
//        userDetails = this.userDetailsService.loadUserByUsername(userEmail);
//        return  jwtService.isTokenValid(jwt, userDetails);
//
//
//    }
//
//    public boolean isUserValidatedByEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
//        final String jwt;
//        final String userEmail;
//        UserDetails userDetails = null;
//        jwt = authorizationHeader.substring(7);
//        userEmail = jwtService.extractUsername(jwt);
//
//        return  jwtService.isTokenValidByEmail(jwt, userEmail);
//
//    }
//
//    public String returnEmail(@RequestHeader(value = "Authorization")String authorizationHeader){
//        final String jwt;
//        final String userEmail;
//        UserDetails userDetails = null;
//        jwt = authorizationHeader.substring(7);
//        userEmail = jwtService.extractUsername(jwt);
//
//        return userEmail;
//    }
//
//    public String returnEmailFromJwt(@RequestHeader(value = "Authorization")String authorizationHeader){
//        final String jwt;
//        jwt = authorizationHeader.substring(7);
//
//        return jwtService.extractUsername(jwt);
//    }
//
//    public String getHeader(@RequestHeader(value = "Authorization")String authorizationHeader){
//        String header = authorizationHeader;
//        return header;
//    }
//
//    public boolean isUserInDatabase(@RequestHeader(value = "Authorization")String authorizationHeader){
//
//
//        final String jwt;
//        jwt = authorizationHeader.substring(7);
//
//        String email = jwtService.extractUsername(jwt);
//
//
//        String newEmail = String.valueOf(clientRepository.findByEmail(email));
//
//        return newEmail != null;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @Transactional
//    public void updateClient(Integer Id, String email){
//        Client client = clientRepository.findById(Id).orElseThrow(() -> new IllegalStateException("User with id: " + Id + " does not exist"));

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
//        client.setEmail(email);
//    }


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
