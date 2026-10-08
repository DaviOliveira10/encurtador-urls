package com.davi.encurtador.service;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeradorCodigoTest {

    private final GeradorCodigo gerador = new GeradorCodigo();

    @Test
    void deveGerarCodigoComSeteCaracteres() {
        assertEquals(7, gerador.gerar().length());
    }

    @Test
    void deveUsarApenasCaracteresAlfanumericos() {
        for (int i = 0; i < 1000; i++) {
            assertTrue(gerador.gerar().matches("[a-zA-Z0-9]{7}"));
        }
    }

    @Test
    void deveGerarCodigosDiferentes() {
        Set<String> codigos = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            codigos.add(gerador.gerar());
        }
        assertEquals(1000, codigos.size());
    }
}
