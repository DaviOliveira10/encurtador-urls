package com.davi.encurtador.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class GeradorCodigo {

    private static final String ALFABETO =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int TAMANHO = 7;

    private final SecureRandom random = new SecureRandom();

    public String gerar() {
        StringBuilder codigo = new StringBuilder(TAMANHO);
        for (int i = 0; i < TAMANHO; i++) {
            codigo.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
        }
        return codigo.toString();
    }
}
