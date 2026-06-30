package com.questionarios.questionarios.dto;

import lombok.Data;
import java.util.List;

// Usado quando o aluno envia todas as respostas
@Data
public class EnvioQuestionarioDTO {
    private String token;
    private List<RespostaDTO> respostas;
}