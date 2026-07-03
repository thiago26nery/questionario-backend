package com.questionarios.questionarios.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "professores")
public class Professor {

    // id gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // coluna obrigatória
    private String nome;

    @Column(nullable = false, unique = true) // valor precisa ser único em toda a tabela
    private String email;

    @Column(nullable = false)
    private String senha;
}