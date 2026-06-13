DROP TABLE IF EXISTS request_generator;

CREATE TABLE IF NOT EXISTS request_generator (
    id              BIGINT          NOT NULL GENERATED ALWAYS AS IDENTITY,
    prefix          VARCHAR(50)     NOT NULL,
    sequence        BIGINT          NOT NULL DEFAULT 0,
    latest_value    VARCHAR(100)    NULL,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_request_generator PRIMARY KEY (id),
    CONSTRAINT uq_request_generator_prefix UNIQUE (prefix)
);