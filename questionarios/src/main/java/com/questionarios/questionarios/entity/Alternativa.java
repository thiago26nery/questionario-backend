package com.questionarios.questionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "alternativas")
public class Alternativa {

    @Id
    // criação de chaves primárias automáticas
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String texto;

    // true = resposta correta
    private Boolean correta;

    @ManyToOne
    @JoinColumn(name = "pergunta_id")
    @JsonIgnoreProperties("alternativas")
    private Pergunta pergunta;
}