package com.questionarios.questionarios.dto;

import lombok.Data;
import java.util.List;

@Data
public class PerguntaDTO {
    private String enunciado;
    private Double pontuacao;
    private List<AlternativaDTO> alternativas;
}