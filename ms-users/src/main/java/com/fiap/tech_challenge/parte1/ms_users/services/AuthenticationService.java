package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service that implements Spring Security's UserDetailsService to load user-specific data.
 * Used during authentication to retrieve user details from the repository by username (login).
 */
@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs an AuthenticationService with the given UserRepository.
     *
     * @param userRepository the user repository used to load user data
     */
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the provided username (login).
     * This method is used by Spring Security during the authentication process.
     *
     * @param username the username (login) identifying the user whose data is required.
     * @return a fully populated UserDetails object (used by Spring Security)
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
