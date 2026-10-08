package com.davi.encurtador.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 7)
    private String codigo;

    @Column(name = "url_original", nullable = false, length = 2048)
    private String urlOriginal;

    @Column(nullable = false)
    private Long cliques = 0L;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    protected Link() {
        // exigido pelo JPA
    }

    public Link(String codigo, String urlOriginal) {
        this.codigo = codigo;
        this.urlOriginal = urlOriginal;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getUrlOriginal() { return urlOriginal; }
    public Long getCliques() { return cliques; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}