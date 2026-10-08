package com.davi.encurtador.exception;

import com.davi.encurtador.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlInvalidaException.class)
    public ResponseEntity<ErroResponse> tratarUrlInvalida(UrlInvalidaException ex) {
        ErroResponse corpo = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                "URL inválida",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpo);
    }

    @ExceptionHandler(LinkNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarLinkNaoEncontrado(LinkNaoEncontradoException ex) {
        ErroResponse corpo = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                "Link não encontrado",
                ex.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
    }
}