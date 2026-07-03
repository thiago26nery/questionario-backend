package com.questionarios.questionarios.repository;

import com.questionarios.questionarios.entity.Alternativa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlternativaRepository extends JpaRepository<Alternativa, Long> {
// Apenas extendem JpaRepository sem métodos extras
}