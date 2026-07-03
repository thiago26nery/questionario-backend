package com.questionarios.questionarios.repository;

import com.questionarios.questionarios.entity.RespostaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RespostaAlunoRepository extends JpaRepository<RespostaAluno, Long> {
    List<RespostaAluno> findByAlunoIdAndPerguntaQuestionarioId(Long alunoId, Long questionarioId);
    // verifica se o aluno já respondeu o questionário
    boolean existsByAlunoIdAndPerguntaQuestionarioId(Long alunoId, Long questionarioId);
    // busca todas as respostas de um questionário para calcular as estatísticas.
    List<RespostaAluno> findByPerguntaQuestionarioId(Long questionarioId);
}