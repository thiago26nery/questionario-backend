package com.questionarios.questionarios.repository;

import com.questionarios.questionarios.entity.TokenAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenAcessoRepository extends JpaRepository<TokenAcesso, Long> {
    Optional<TokenAcesso> findByToken(String token);
}