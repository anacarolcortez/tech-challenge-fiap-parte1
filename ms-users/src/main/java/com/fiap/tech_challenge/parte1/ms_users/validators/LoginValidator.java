package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.LoginAlreadyExistsException;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UsersRepository;
import org.springframework.stereotype.Component;

@Component
public class LoginValidator implements UserValidator {

    private final UsersRepository usersRepository;

    public LoginValidator(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public void validate(UsersRequestDTO dto) {
        if (usersRepository.existsByLogin(dto.login())) {
            throw new LoginAlreadyExistsException("O login informado já está em uso.");
        }
    }
}

