package com.fiap.tech_challenge.parte1.ms_users.entities;

import java.util.UUID;

/**
 * Represents a physical address associated with a user.
 */
public class Address {

    private UUID id;
    private String zipcode;
    private String street;
    private int number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String userId;

    /**
     * Default constructor for Address.
     */
    public Address() {
    }

    /**
     * Returns the unique identifier of this address.
     *
     * @return the UUID of the address
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the postal code (zipcode) of this address.
     *
     * @return the zipcode as a String
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * Returns the street name of this address.
     *
     * @return the street name as a String
     */
    public String getStreet() {
        return street;
    }

    /**
     * Returns the street number of this address.
     *
     * @return the street number as an int
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the complement information of this address (e.g., apartment, suite).
     *
     * @return the complement as a String, or null if none
     */
    public String getComplement() {
        return complement;
    }

    /**
     * Returns the neighborhood of this address.
     *
     * @return the neighborhood as a String
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Returns the city where this address is located.
     *
     * @return the city as a String
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the state where this address is located.
     *
     * @return the state as a String
     */
    public String getState() {
        return state;
    }

    /**
     * Returns the identifier of the user associated with this address.
     *
     * @return the user ID as a String
     */
    public String getUserId() {
        return userId;
    }
}
