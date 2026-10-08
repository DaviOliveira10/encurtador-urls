package com.davi.encurtador.service;

import com.davi.encurtador.exception.LinkNaoEncontradoException;
import org.springframework.transaction.annotation.Transactional;
import com.davi.encurtador.exception.UrlInvalidaException;
import com.davi.encurtador.model.Link;
import com.davi.encurtador.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class LinkService {

    private static final int MAX_TENTATIVAS = 5;
    private static final int MAX_TAMANHO_URL = 2048;

    private final LinkRepository repository;
    private final GeradorCodigo gerador;
    private final String baseUrl;

    public LinkService(LinkRepository repository,
                       GeradorCodigo gerador,
                       @Value("${app.base-url}") String baseUrl) {
        this.repository = repository;
        this.gerador = gerador;
        this.baseUrl = baseUrl.replaceAll("/+$", "");
    }

    public Link criar(String url) {
        String urlLimpa = validar(url);

        for (int i = 0; i < MAX_TENTATIVAS; i++) {
            String codigo = gerador.gerar();
            if (!repository.existsByCodigo(codigo)) {
                return repository.save(new Link(codigo, urlLimpa));
            }
        }
        throw new IllegalStateException("Não foi possível gerar um código único");
    }

    public String montarLinkCurto(Link link) {
        return baseUrl + "/" + link.getCodigo();
    }

    @Transactional
    public String registrarClique(String codigo) {
        int atualizados = repository.incrementarCliques(codigo);
        if (atualizados == 0) {
            throw new LinkNaoEncontradoException(codigo);
        }
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new LinkNaoEncontradoException(codigo))
                .getUrlOriginal();
    }

    @Transactional(readOnly = true)
    public Link buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new LinkNaoEncontradoException(codigo));
    }

    private String validar(String url) {
        if (url == null || url.isBlank()) {
            throw new UrlInvalidaException("A URL não pode ser vazia");
        }
        String urlLimpa = url.trim();
        if (urlLimpa.length() > MAX_TAMANHO_URL) {
            throw new UrlInvalidaException("A URL deve ter no máximo 2048 caracteres");
        }

        URI uri;
        try {
            uri = new URI(urlLimpa);
        } catch (URISyntaxException e) {
            throw new UrlInvalidaException("A URL informada não é válida");
        }

        String esquema = uri.getScheme();
        if (esquema == null
                || !(esquema.equalsIgnoreCase("http") || esquema.equalsIgnoreCase("https"))) {
            throw new UrlInvalidaException("A URL deve começar com http:// ou https://");
        }
        if (uri.getHost() == null || uri.getHost().isBlank()) {
            throw new UrlInvalidaException("A URL precisa ter um endereço válido");
        }
        if (apontaParaOProprioSistema(uri)) {
            throw new UrlInvalidaException("Não é possível encurtar links deste próprio site");
        }
        return urlLimpa;
    }

    private boolean apontaParaOProprioSistema(URI uri) {
        try {
            String hostDoSistema = new URI(baseUrl).getHost();
            return hostDoSistema != null && hostDoSistema.equalsIgnoreCase(uri.getHost());
        } catch (URISyntaxException e) {
            return false;
        }
    }
}