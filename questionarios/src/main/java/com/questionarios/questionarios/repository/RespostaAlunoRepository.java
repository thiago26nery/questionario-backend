package com.questionarios.questionarios.repository;

import com.questionarios.questionarios.entity.RespostaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RespostaAlunoRepository extends JpaRepository<RespostaAluno, Long> {
    List<RespostaAluno> findByAlunoIdAndPerguntaQuestionarioId(Long alunoId, Long questionarioId);
    boolean existsByAlunoIdAndPerguntaQuestionarioId(Long alunoId, Long questionarioId);

    List<RespostaAluno> findByPerguntaQuestionarioId(Long questionarioId);
}