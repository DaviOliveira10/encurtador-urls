package com.davi.encurtador.service;

import com.davi.encurtador.exception.UrlInvalidaException;
import com.davi.encurtador.model.Link;
import com.davi.encurtador.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    private static final String BASE_URL = "https://meusite.onrender.com";
    private static final String URL_VALIDA = "https://www.youtube.com/watch?v=abc123";

    @Mock
    private LinkRepository repository;

    @Mock
    private GeradorCodigo gerador;

    private LinkService service;

    @BeforeEach
    void preparar() {
        service = new LinkService(repository, gerador, BASE_URL);
    }

    @Test
    void deveSalvarLinkComCodigoGerado() {
        when(gerador.gerar()).thenReturn("abc1234");
        when(repository.existsByCodigo("abc1234")).thenReturn(false);
        when(repository.save(any(Link.class))).thenAnswer(inv -> inv.getArgument(0));

        Link link = service.criar(URL_VALIDA);

        assertEquals("abc1234", link.getCodigo());
        assertEquals(URL_VALIDA, link.getUrlOriginal());
        assertEquals(0L, link.getCliques());
    }

    @Test
    void deveGerarOutroCodigoQuandoHaColisao() {
        when(gerador.gerar()).thenReturn("aaaaaaa", "bbbbbbb");
        when(repository.existsByCodigo("aaaaaaa")).thenReturn(true);
        when(repository.existsByCodigo("bbbbbbb")).thenReturn(false);
        when(repository.save(any(Link.class))).thenAnswer(inv -> inv.getArgument(0));

        Link link = service.criar(URL_VALIDA);

        assertEquals("bbbbbbb", link.getCodigo());
    }

    @Test
    void deveFalharDepoisDeCincoColisoes() {
        when(gerador.gerar()).thenReturn("aaaaaaa");
        when(repository.existsByCodigo("aaaaaaa")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.criar(URL_VALIDA));
        verify(repository, never()).save(any());
    }

    @Test
    void deveRejeitarUrlVazia() {
        assertThrows(UrlInvalidaException.class, () -> service.criar("   "));
        verifyNoInteractions(repository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ftp://site.com", "site.com", "javascript:alert(1)", "https://"})
    void deveRejeitarUrlsInvalidas(String url) {
        assertThrows(UrlInvalidaException.class, () -> service.criar(url));
        verifyNoInteractions(repository);
    }

    @Test
    void deveRejeitarUrlMuitoGrande() {
        String urlGigante = "https://site.com/" + "a".repeat(2048);

        assertThrows(UrlInvalidaException.class, () -> service.criar(urlGigante));
    }

    @Test
    void deveRejeitarUrlDoProprioSistema() {
        assertThrows(UrlInvalidaException.class,
                () -> service.criar(BASE_URL + "/x7Kp2Ab"));
    }

    @Test
    void deveMontarLinkCurtoCompleto() {
        Link link = new Link("x7Kp2Ab", URL_VALIDA);

        assertEquals("https://meusite.onrender.com/x7Kp2Ab", service.montarLinkCurto(link));
    }
}
