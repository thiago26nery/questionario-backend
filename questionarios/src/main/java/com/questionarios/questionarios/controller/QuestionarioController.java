package com.questionarios.questionarios.controller;

import com.questionarios.questionarios.dto.EnvioQuestionarioDTO;
import com.questionarios.questionarios.dto.QuestionarioDTO;
import com.questionarios.questionarios.entity.Questionario;
import com.questionarios.questionarios.service.QuestionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/questionarios")
@Tag(name = "Questionários", description = "Endpoints para questionários")
public class QuestionarioController {

    @Autowired
    private QuestionarioService questionarioService;

    @Operation(summary = "Criar questionário (professor autenticado)")
    @PostMapping("/criar")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EntityModel<Questionario>> criar(
            @RequestBody QuestionarioDTO dto,
            Authentication auth) {

        // Pega o email do professor pelo token JWT
        String emailProfessor = auth.getName();
        Questionario questionario = questionarioService.criar(dto, emailProfessor);

        EntityModel<Questionario> model = EntityModel.of(questionario);
        model.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(QuestionarioController.class)
                        .listar())
                .withRel("todos-questionarios"));
        model.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(QuestionarioController.class)
                        .estatisticas(questionario.getId()))
                .withRel("estatisticas"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Enviar questionário para aluno por e-mail")
    @PostMapping("/{questionarioId}/enviar/{alunoId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, String>> enviarParaAluno(
            @PathVariable Long questionarioId,
            @PathVariable Long alunoId,
            Authentication auth) {

        String msg = questionarioService.enviarParaAluno(questionarioId, alunoId);
        return ResponseEntity.ok(Map.of("mensagem", msg));
    }

    @Operation(summary = "Acessar questionário pelo token (aluno)")
    @GetMapping("/responder")
    public ResponseEntity<Questionario> acessar(@RequestParam String token) {
        return ResponseEntity.ok(questionarioService.acessarPorToken(token));
    }

    @Operation(summary = "Enviar respostas do aluno")
    @PostMapping("/responder")
    public ResponseEntity<Map<String, Object>> responder(
            @RequestBody EnvioQuestionarioDTO dto) {

        return ResponseEntity.ok(questionarioService.corrigir(dto));
    }

    @Operation(summary = "Ver estatísticas do questionário")
    @GetMapping("/{id}/estatisticas")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Map<String, Object>> estatisticas(@PathVariable Long id) {
        return ResponseEntity.ok(questionarioService.estatisticas(id));
    }

    @Operation(summary = "Listar todos os questionários")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Questionario>> listar() {
        return ResponseEntity.ok(questionarioService.listar());
    }
}