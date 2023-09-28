package com.tradingSystem.clientService.clients.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client implements UserDetails {

    @jakarta.persistence.Id
    @GeneratedValue//(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    private Integer Id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = true)
    private Integer defaultPortfolioId;
    private Integer accountId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
        //return List.of(new SimpleGrantedAuthority(role.name()));
    }


    public Integer getDefaultPortfolioId() {
        return defaultPortfolioId;
    }

    public void setDefaultPortfolioId(Integer defaultPortfolioId) {
        this.defaultPortfolioId = defaultPortfolioId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}