package com.questionarios.questionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "perguntas")
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String enunciado;

    private Double pontuacao;

    @ManyToOne
    @JoinColumn(name = "questionario_id")
    @JsonIgnoreProperties("perguntas")
    private Questionario questionario;

    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pergunta")
    private List<Alternativa> alternativas;
}