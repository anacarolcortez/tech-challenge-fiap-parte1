package com.fiap.tech_challenge.parte1.ms_users.entities;


import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Users {
    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;
    private Date dateLastChange;
    private List<Address> address;
    private Boolean status;
    private Role role;

    public Users(String name, String email, String login, String password, Role role) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.dateLastChange = new Date();
        this.status = true;
        this.role = role;
    }

    public Users() {
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

    public String getPassword() {
        return password;
    }

    public Date getDateLastChange() {
        return dateLastChange;
    }

    public List<Address> getAddress() {
        return address;
    }

    public Boolean getStatus() {
        return status;
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
