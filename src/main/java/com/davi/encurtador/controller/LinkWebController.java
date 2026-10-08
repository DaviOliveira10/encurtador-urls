package com.davi.encurtador.controller;

import com.davi.encurtador.exception.UrlInvalidaException;
import com.davi.encurtador.model.Link;
import com.davi.encurtador.service.LinkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LinkWebController {

    private final LinkService service;

    public LinkWebController(LinkService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @PostMapping("/encurtar")
    public String encurtar(@RequestParam String url, Model model) {
        model.addAttribute("urlDigitada", url);
        try {
            Link link = service.criar(url);
            model.addAttribute("linkCurto", service.montarLinkCurto(link));
        } catch (UrlInvalidaException e) {
            model.addAttribute("erro", e.getMessage());
        }
        return "index";
    }
}
