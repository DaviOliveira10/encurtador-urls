package com.davi.encurtador.repository;

import com.davi.encurtador.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    @Modifying
    @Query("UPDATE Link l SET l.cliques = l.cliques + 1 WHERE l.codigo = :codigo")
    int incrementarCliques(@Param("codigo") String codigo);
}