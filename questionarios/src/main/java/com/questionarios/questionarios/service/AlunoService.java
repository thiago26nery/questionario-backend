package com.questionarios.questionarios.service;

import com.questionarios.questionarios.dto.AlunoDTO;
import com.questionarios.questionarios.entity.Aluno;
import com.questionarios.questionarios.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    // Cadastra um novo aluno
    public Aluno cadastrar(AlunoDTO dto) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setEmail(dto.getEmail());
        aluno.setMatricula(dto.getMatricula());
        return alunoRepository.save(aluno);
    }

    // Lista todos os alunos
    public List<Aluno> listar() {
        return alunoRepository.findAll();
    }

    // Busca aluno por ID
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }
}