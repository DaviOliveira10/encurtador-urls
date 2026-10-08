CREATE TABLE links (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    codigo       VARCHAR(7)    NOT NULL,
    url_original VARCHAR(2048) NOT NULL,
    cliques      BIGINT        NOT NULL DEFAULT 0,
    criado_em    DATETIME      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_links_codigo UNIQUE (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;