package com.questionarios.questionarios.service;

import com.questionarios.questionarios.dto.*;
import com.questionarios.questionarios.entity.*;
import com.questionarios.questionarios.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuestionarioService {

    @Autowired
    private QuestionarioRepository questionarioRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TokenAcessoRepository tokenAcessoRepository;

    @Autowired
    private RespostaAlunoRepository respostaAlunoRepository;

    @Autowired
    private EmailService emailService;

    // Cria um questionário com perguntas e alternativas
    public Questionario criar(QuestionarioDTO dto, String emailProfessor) {
        Professor professor = professorRepository.findByEmail(emailProfessor)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));

        Questionario questionario = new Questionario();
        questionario.setTitulo(dto.getTitulo());
        questionario.setDescricao(dto.getDescricao());
        questionario.setValidadeTokenMinutos(dto.getValidadeTokenMinutos());
        questionario.setProfessor(professor);

        // Cria as perguntas e alternativas
        List<Pergunta> perguntas = new ArrayList<>();
        for (PerguntaDTO pDto : dto.getPerguntas()) {
            Pergunta pergunta = new Pergunta();
            pergunta.setEnunciado(pDto.getEnunciado());
            pergunta.setPontuacao(pDto.getPontuacao());
            pergunta.setQuestionario(questionario);

            List<Alternativa> alternativas = new ArrayList<>();
            for (AlternativaDTO aDto : pDto.getAlternativas()) {
                Alternativa alternativa = new Alternativa();
                alternativa.setTexto(aDto.getTexto());
                alternativa.setCorreta(aDto.getCorreta());
                alternativa.setPergunta(pergunta);
                alternativas.add(alternativa);
            }
            pergunta.setAlternativas(alternativas);
            perguntas.add(pergunta);
        }
        questionario.setPerguntas(perguntas);
        // salva em cascata
        return questionarioRepository.save(questionario);
    }

    // Associa questionário ao aluno e envia e-mail com token
    public String enviarParaAluno(Long questionarioId, Long alunoId) {
        Questionario questionario = questionarioRepository.findById(questionarioId)
                .orElseThrow(() -> new RuntimeException("Questionário não encontrado"));

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        // Gera token único
        String tokenValor = UUID.randomUUID().toString();

        TokenAcesso token = new TokenAcesso();
        token.setToken(tokenValor);
        token.setAluno(aluno);
        token.setQuestionario(questionario);
        token.setUsado(false);
        // calcula a dataExpiracao somando validadeTokenMinutos ao momento atual
        token.setDataExpiracao(
                LocalDateTime.now().plusMinutes(questionario.getValidadeTokenMinutos())
        );
        tokenAcessoRepository.save(token);

        // Envia e-mail para o aluno
        emailService.enviarLinkQuestionario(aluno.getEmail(), tokenValor, aluno.getNome());

        return "E-mail enviado para " + aluno.getEmail();
    }

    // Valida o token e retorna o questionário para o aluno responder
    public Questionario acessarPorToken(String tokenValor) {
        TokenAcesso token = tokenAcessoRepository.findByToken(tokenValor)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Verifica se já foi usado
        if (token.getUsado()) {
            throw new RuntimeException("Este questionário já foi respondido");
        }

        // Verifica se expirou
        if (LocalDateTime.now().isAfter(token.getDataExpiracao())) {
            throw new RuntimeException("Token expirado");
        }

        return token.getQuestionario();
    }

    // Corrige as respostas e calcula a nota
    public Map<String, Object> corrigir(EnvioQuestionarioDTO dto) {
        TokenAcesso token = tokenAcessoRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (token.getUsado()) {
            throw new RuntimeException("Questionário já respondido");
        }

        if (LocalDateTime.now().isAfter(token.getDataExpiracao())) {
            throw new RuntimeException("Token expirado");
        }

        // Verifica se aluno já respondeu (RN04)
        boolean jaRespondeu = respostaAlunoRepository
                .existsByAlunoIdAndPerguntaQuestionarioId(
                        token.getAluno().getId(),
                        token.getQuestionario().getId()
                );
        if (jaRespondeu) {
            throw new RuntimeException("Questionário já respondido");
        }

        int acertos = 0;
        int erros = 0;
        double notaTotal = 0;

        // Percorre cada resposta enviada pelo aluno
        for (RespostaDTO respostaDTO : dto.getRespostas()) {
            Pergunta pergunta = token.getQuestionario().getPerguntas()
                    .stream()
                    .filter(p -> p.getId().equals(respostaDTO.getPerguntaId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada"));

            Alternativa alternativa = pergunta.getAlternativas()
                    .stream()
                    .filter(a -> a.getId().equals(respostaDTO.getAlternativaId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Alternativa não encontrada"));

            // verifica se a alternativa é a correta
            boolean correta = alternativa.getCorreta();

            // Salva a resposta do aluno
            RespostaAluno resposta = new RespostaAluno();
            resposta.setAluno(token.getAluno());
            resposta.setPergunta(pergunta);
            resposta.setAlternativaEscolhida(alternativa);
            resposta.setCorreta(correta);
            respostaAlunoRepository.save(resposta);

            if (correta) {
                acertos++;
                notaTotal += pergunta.getPontuacao();
            } else {
                erros++;
            }
        }

        // Marca o token como usado
        token.setUsado(true);
        tokenAcessoRepository.save(token);

        int total = acertos + erros;
        double percentual = total > 0 ? (acertos * 100.0) / total : 0;

        // Monta o resultado
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("notaFinal", notaTotal);
        resultado.put("acertos", acertos);
        resultado.put("erros", erros);
        resultado.put("percentualAproveitamento", String.format("%.1f%%", percentual));
        resultado.put("linkRespostas",
                "http://localhost:8080/questionarios/respostas?token=" + dto.getToken());
        resultado.put("linkEstatisticas",
                "http://localhost:8080/questionarios/" +
                        token.getQuestionario().getId() + "/estatisticas");

        return resultado;
    }

    // Retorna estatísticas do questionário
    public Map<String, Object> estatisticas(Long questionarioId) {
        Questionario questionario = questionarioRepository.findById(questionarioId)
                .orElseThrow(() -> new RuntimeException("Questionário não encontrado"));

        Map<String, Object> stats = new LinkedHashMap<>();
        List<Map<String, Object>> perguntasStats = new ArrayList<>();

        for (Pergunta pergunta : questionario.getPerguntas()) {
            List<RespostaAluno> respostas = respostaAlunoRepository
                    .findByPerguntaQuestionarioId(questionarioId)
                    .stream()
                    .filter(r -> r.getPergunta().getId().equals(pergunta.getId()))
                    .toList();

            long totalRespostas = respostas.size();
            long corretas = respostas.stream().filter(RespostaAluno::getCorreta).count();
            long incorretas = totalRespostas - corretas;
            double percentualAcerto = totalRespostas > 0
                    ? (corretas * 100.0) / totalRespostas : 0;

            Map<String, Object> pStat = new LinkedHashMap<>();
            pStat.put("pergunta", pergunta.getEnunciado());
            pStat.put("totalRespostas", totalRespostas);
            pStat.put("corretas", corretas);
            pStat.put("incorretas", incorretas);
            pStat.put("percentualAcerto", String.format("%.1f%%", percentualAcerto));
            perguntasStats.add(pStat);
        }

        stats.put("questionario", questionario.getTitulo());
        stats.put("estatisticasPorPergunta", perguntasStats);
        return stats;
    }

    // Lista todos os questionários
    public List<Questionario> listar() {
        return questionarioRepository.findAll();
    }
}