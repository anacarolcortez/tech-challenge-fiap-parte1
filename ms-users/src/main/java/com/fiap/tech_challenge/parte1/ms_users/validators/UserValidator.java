package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;

public interface UserValidator {
    void validate(UsersRequestDTO dto);
}
