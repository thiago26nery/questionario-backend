package com.questionarios.questionarios.controller;

import com.questionarios.questionarios.dto.LoginDTO;
import com.questionarios.questionarios.dto.ProfessorDTO;
import com.questionarios.questionarios.entity.Professor;
import com.questionarios.questionarios.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/professores")
@Tag(name = "Professores", description = "Endpoints para professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    //POST /cadastrar: recebe ProfessorDTO,
    // chama professorService.cadastrar()
    @Operation(summary = "Cadastrar professor")
    @PostMapping("/cadastrar")
    public ResponseEntity<EntityModel<Professor>> cadastrar(@RequestBody ProfessorDTO dto) {
        Professor professor = professorService.cadastrar(dto);

        // HATEOAS - adiciona links na resposta
        EntityModel<Professor> model = EntityModel.of(professor);
        model.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ProfessorController.class).login(null))
                .withRel("login"));

        return ResponseEntity.ok(model);
    }

    // recebe LoginDTO, chama professorService.login(),
    // retorna Map com a chave 'token' e o valor JWT
    @Operation(summary = "Login do professor")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO dto) {
        String token = professorService.login(dto);
        return ResponseEntity.ok(Map.of("token", token));
    }
}