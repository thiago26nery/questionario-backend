package com.questionarios.questionarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

// Padrão de resposta para qualquer erro da API
@Data
@AllArgsConstructor
public class ErroResponseDTO {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;
}