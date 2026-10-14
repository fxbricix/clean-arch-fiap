package com.fiap.cleanarchitecture.infrastructure.web.controllers.exceptions;

import com.fiap.cleanarchitecture.application.exceptions.ConflitoException;
import com.fiap.cleanarchitecture.application.exceptions.RecursoNaoEncontradoException;
import com.fiap.cleanarchitecture.domain.exceptions.DominioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> naoEncontrado(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ProblemDetail> conflito(ConflitoException e) {
        return resposta(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(DominioException.class)
    public ResponseEntity<ProblemDetail> dominio(DominioException e) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ProblemDetail> resposta(HttpStatus status, String detalhe) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detalhe));
    }
}