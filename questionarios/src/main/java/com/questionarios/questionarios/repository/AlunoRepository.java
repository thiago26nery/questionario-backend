package com.questionarios.questionarios.repository;

import com.questionarios.questionarios.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    // busca aluno pelo e-mail
    Optional<Aluno> findByEmail(String email);
}