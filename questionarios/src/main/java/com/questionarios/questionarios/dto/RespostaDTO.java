package com.questionarios.questionarios.dto;

import lombok.Data;

// Representa a resposta do aluno para uma pergunta
@Data
public class RespostaDTO {
    private Long perguntaId;
    private Long alternativaId;
}