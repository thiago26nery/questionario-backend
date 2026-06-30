package com.questionarios.questionarios.exception;

import com.questionarios.questionarios.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// Intercepta erros de qualquer Controller e padroniza a resposta
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura qualquer RuntimeException lançada nos Services
    // (ex: "Token inválido", "Questionário já respondido", "Aluno não encontrado")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDTO> tratarRuntimeException(RuntimeException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}