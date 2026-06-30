package com.questionarios.questionarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Envia o e-mail com o link do questionário para o aluno
    public void enviarLinkQuestionario(String emailDestino, String token, String nomeAluno) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDestino);
        message.setSubject("Questionário disponível para você!");
        message.setText(
                "Olá, " + nomeAluno + "!\n\n" +
                        "Um questionário foi disponibilizado para você.\n\n" +
                        "Acesse pelo link abaixo:\n" +
                        "http://localhost:8080/questionarios/responder?token=" + token + "\n\n" +
                        "Atenção: este link expira conforme o tempo configurado pelo professor.\n\n" +
                        "Bons estudos!"
        );
        mailSender.send(message);
    }
}