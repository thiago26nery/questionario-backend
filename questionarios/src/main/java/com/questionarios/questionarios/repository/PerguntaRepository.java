package com.questionarios.questionarios.repository;

import com.questionarios.questionarios.entity.Pergunta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {
// Apenas extendem JpaRepository sem métodos extras
}