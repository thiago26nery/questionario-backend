package com.questionarios.questionarios.controller;

import com.questionarios.questionarios.dto.AlunoDTO;
import com.questionarios.questionarios.entity.Aluno;
import com.questionarios.questionarios.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@Tag(name = "Alunos", description = "Endpoints para alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @Operation(summary = "Cadastrar aluno")
    @PostMapping("/cadastrar")
    public ResponseEntity<EntityModel<Aluno>> cadastrar(@RequestBody AlunoDTO dto) {
        Aluno aluno = alunoService.cadastrar(dto);

        EntityModel<Aluno> model = EntityModel.of(aluno);
        model.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(AlunoController.class).listar())
                .withRel("todos-alunos"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Listar todos os alunos")
    @GetMapping
    public ResponseEntity<List<Aluno>> listar() {
        return ResponseEntity.ok(alunoService.listar());
    }

    @Operation(summary = "Buscar aluno por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }
}