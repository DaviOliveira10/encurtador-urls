package com.davi.encurtador.exception;

public class UrlInvalidaException extends RuntimeException {

    public UrlInvalidaException(String mensagem) {
        super(mensagem);
    }
}