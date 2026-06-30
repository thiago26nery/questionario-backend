package com.questionarios.questionarios.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "respostas_aluno")
public class RespostaAluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "pergunta_id")
    private Pergunta pergunta;

    @ManyToOne
    @JoinColumn(name = "alternativa_escolhida_id")
    private Alternativa alternativaEscolhida;

    // Calculado automaticamente: true se acertou
    private Boolean correta;
}