package com.fiap.tech_challenge.parte1.ms_users.entities;


import java.util.List;
import java.util.Date;

public class Users {
    private String name;
    private String email;
    private String login;
    private String password;
    private Date dateLastChange;
    private List <Address> address;
    private Boolean status;
    private Role role;

    public Users(String name, String email, String login, String password, Date dateLastChange, List address, Boolean status, Role role) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.dateLastChange = dateLastChange;
        this.address = address;
        this.status = status;
        this.role = role;
    }

    public Users() {

    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public Date getDateLastChange() {
        return this.dateLastChange;
    }

    public List<Address> getAddress() {
        return this.address;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Role getRole() {
        return this.role;
    }
}
