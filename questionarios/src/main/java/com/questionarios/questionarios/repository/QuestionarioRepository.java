package com.questionarios.questionarios.repository;

import com.questionarios.questionarios.entity.Questionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionarioRepository extends JpaRepository<Questionario, Long> {
// Apenas extendem JpaRepository sem métodos extras
}