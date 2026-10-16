package com.fiap.cleanarchitecture.domain.entities;

import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import lombok.Getter;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

@Getter
public class Email {
    String email;

    public Email(String email) {
        isEmailValido(email);
        this.email = email.toLowerCase();
    }

    public void isEmailValido(String email) {
        EmailValidator emailValidator = new EmailValidator();
        if (email == null || email.isEmpty()) {
            throw new DominioException("Email não pode ser nulo ou vazio");
        }
        if (!emailValidator.isValid(email, null)) {
            throw new DominioException("Email inválido");
        }
    }
}
