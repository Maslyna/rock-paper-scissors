CREATE EXTENSION IF NOT EXISTS pgcrypto;

DROP TABLE IF EXISTS t_rounds;

CREATE TABLE t_rounds
(
    round_id   UUID PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    move_a     SMALLINT                                   NOT NULL,
    move_b     SMALLINT                                   NOT NULL,
    result     SMALLINT                                   NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE                NOT NULL

);