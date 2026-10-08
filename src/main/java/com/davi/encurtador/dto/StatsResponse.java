package com.davi.encurtador.dto;

import java.time.LocalDateTime;

public record StatsResponse(String codigo, String urlOriginal, Long cliques, LocalDateTime criadoEm) {
}