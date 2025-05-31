package com.fiap.tech_challenge.parte1.ms_users.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user entity with authentication and authorization details.
 * Implements {@link UserDetails} to integrate with Spring Security.
 */
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

    /**
     * Constructs a new User with specified details.
     *
     * @param name     the user's full name
     * @param email    the user's email address
     * @param login    the login username
     * @param password the password (hashed)
     * @param role     the role assigned to the user
     */
    public User(String name, String email, String login, String password, Role role) {
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.dateLastChange = new Date();
        this.active = true;
        this.role = role;
    }

    /**
     * Default constructor for frameworks.
     */
    public User() {
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user's UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Returns the full name of the user.
     *
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's email address.
     *
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the login username.
     *
     * @return user's login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return user's password (hashed)
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used to authenticate the user.
     * Here it returns the login.
     *
     * @return user's login
     */
    @Override
    public String getUsername() {
        return login;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return {@code true} if the account is non-expired, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return {@code true} if the user is not locked, {@code false} otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return {@code true} if credentials are valid, {@code false} otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return {@code true} if the user is active, {@code false} otherwise
     */
    @Override
    public boolean isEnabled() {
        return this.active != null && this.active;
    }

    /**
     * Returns the date when the user information was last changed.
     *
     * @return date of last change
     */
    public Date getDateLastChange() {
        return dateLastChange;
    }

    /**
     * Returns the user's primary address.
     *
     * @return the main address
     * @throws IndexOutOfBoundsException if the user has no addresses
     */
    public Address getMainAddress() {
        return address.get(0);
    }

    /**
     * Returns whether the user is currently active.
     *
     * @return true if active, false otherwise
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Returns the role assigned to the user.
     *
     * @return user's role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Returns the list of addresses associated with the user.
     *
     * @return list of addresses
     */
    public List<Address> getAddresses() {
        return address;
    }

    /**
     * Sets the addresses of the user.
     *
     * @param address list of addresses to set
     */
    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
