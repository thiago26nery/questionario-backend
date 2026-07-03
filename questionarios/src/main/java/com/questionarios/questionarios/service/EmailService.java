package com.questionarios.questionarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired // credenciais do gmail
    private JavaMailSender mailSender;

    // Envia o e-mail simulado no console para não estourar erro de SMTP
    // contem o token
    public void enviarLinkQuestionario(String emailDestino, String token, String nomeAluno) {
        System.out.println("====== [SIMULAÇÃO DE E-MAIL ENVIADO] ======");
        System.out.println("Para: " + emailDestino);
        System.out.println("Assunto: Questionário disponível para você!");
        System.out.println("Mensagem:\nOlá, " + nomeAluno + "!\n\n" +
                "Um questionário foi disponibilizado para você.\n\n" +
                "Acesse pelo link abaixo:\n" +
                "http://localhost:8080/questionarios/responder?token=" + token + "\n\n" +
                "Atenção: este link expira conforme o tempo configurado pelo professor.\n\n" +
                "Bons estudos!");
        System.out.println("===========================================");
    }
}