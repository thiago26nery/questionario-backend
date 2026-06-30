package com.questionarios.questionarios.service;

import com.questionarios.questionarios.dto.LoginDTO;
import com.questionarios.questionarios.dto.ProfessorDTO;
import com.questionarios.questionarios.entity.Professor;
import com.questionarios.questionarios.repository.ProfessorRepository;
import com.questionarios.questionarios.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Cadastra um novo professor
    public Professor cadastrar(ProfessorDTO dto) {
        Professor professor = new Professor();
        professor.setNome(dto.getNome());
        professor.setEmail(dto.getEmail());
        // Criptografa a senha antes de salvar
        professor.setSenha(passwordEncoder.encode(dto.getSenha()));
        return professorRepository.save(professor);
    }

    // Realiza o login e retorna o token JWT
    public String login(LoginDTO dto) {
        Professor professor = professorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        // Verifica se a senha está correta
        if (!passwordEncoder.matches(dto.getSenha(), professor.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }

        return jwtUtil.gerarToken(professor.getEmail());
    }
}