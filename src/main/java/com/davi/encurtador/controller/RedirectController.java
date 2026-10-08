package com.davi.encurtador.controller;

import com.davi.encurtador.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@Controller
public class RedirectController {

    private final LinkService service;

    public RedirectController(LinkService service) {
        this.service = service;
    }

    @GetMapping("/{codigo:[a-zA-Z0-9]{7}}")
    public ResponseEntity<Void> redirecionar(@PathVariable String codigo) {
        String destino = service.registrarClique(codigo);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(destino))
                .build();
    }
}
