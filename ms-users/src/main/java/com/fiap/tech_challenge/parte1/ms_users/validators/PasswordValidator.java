package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.ChangePasswordRequestDTO;

public interface PasswordValidator {
    void validate(ChangePasswordRequestDTO dto, String oldPassword);
}
