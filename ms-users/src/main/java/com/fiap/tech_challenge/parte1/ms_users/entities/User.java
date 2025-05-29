package com.fiap.tech_challenge.parte1.ms_users.entities;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User implements UserDetails {
    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;
    private Date dateLastChange;
    private List<Address> address;
    private Boolean active;
    private Role role;

    public User(String name, String email, String login, String password, Role role) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.dateLastChange = new Date();
        this.active = true;
        this.role = role;
    }

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }


    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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
        return this.active != null && this.active;
    }

    public Date getDateLastChange() {
        return dateLastChange;
    }

    public Address getMainAddress() {
        return address.get(0);
    }

    public Boolean getActive() {
        return active;
    }

    public Role getRole() {
        return role;
    }

    public List<Address> getAddresses() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
