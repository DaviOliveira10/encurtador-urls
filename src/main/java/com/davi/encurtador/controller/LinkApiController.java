package com.davi.encurtador.controller;

import com.davi.encurtador.dto.CriarLinkRequest;
import com.davi.encurtador.dto.LinkResponse;
import com.davi.encurtador.dto.StatsResponse;
import com.davi.encurtador.model.Link;
import com.davi.encurtador.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
public class LinkApiController {

    private final LinkService service;

    public LinkApiController(LinkService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LinkResponse> criar(@RequestBody CriarLinkRequest request) {
        Link link = service.criar(request.url());
        LinkResponse resposta = new LinkResponse(
                link.getCodigo(),
                service.montarLinkCurto(link),
                link.getUrlOriginal());
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping("/{codigo}/stats")
    public StatsResponse estatisticas(@PathVariable String codigo) {
        Link link = service.buscarPorCodigo(codigo);
        return new StatsResponse(
                link.getCodigo(),
                link.getUrlOriginal(),
                link.getCliques(),
                link.getCriadoEm());
    }
}