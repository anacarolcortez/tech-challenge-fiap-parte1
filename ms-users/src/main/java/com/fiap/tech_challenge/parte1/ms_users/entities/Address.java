package com.fiap.tech_challenge.parte1.ms_users.entities;

import java.util.UUID;

public class Address {
    private UUID id;
    private String zipcode;
    private String street;
    private int number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;

    public Address(UUID id, String zipcode, String street, int number, String complement, String neighborhood, String city, String state) {
        this.id = id;
        this.zipcode = zipcode;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
