package com.questionarios.questionarios.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "questionarios")
public class Questionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    private LocalDateTime dataCriacao;

    // Tempo em minutos para o token expirar
    private Integer validadeTokenMinutos;

    @ManyToOne // professor cria
    @JoinColumn(name = "professor_id")
    @JsonIgnoreProperties("questionarios") // evita referencia circular infinita
    private Professor professor;

    // lista de perguntas
    // repita automaticamente (em cascata) na entidade relacionada.
    @OneToMany(mappedBy = "questionario", cascade = CascadeType.ALL)
    private List<Pergunta> perguntas;

    // Executado automaticamente antes de salvar
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }
}