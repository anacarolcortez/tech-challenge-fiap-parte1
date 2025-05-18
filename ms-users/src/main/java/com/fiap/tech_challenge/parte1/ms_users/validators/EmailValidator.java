package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.EmailAlreadyExistsException;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements UserValidator {

    private final UserRepository userRepository;

    public EmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UsersRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("O e-mail informado já está em uso.");
        }
    }
}

