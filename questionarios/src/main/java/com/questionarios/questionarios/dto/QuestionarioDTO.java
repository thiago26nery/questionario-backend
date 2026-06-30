package com.questionarios.questionarios.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionarioDTO {
    private String titulo;
    private String descricao;
    private Integer validadeTokenMinutos;
    private List<PerguntaDTO> perguntas;
}