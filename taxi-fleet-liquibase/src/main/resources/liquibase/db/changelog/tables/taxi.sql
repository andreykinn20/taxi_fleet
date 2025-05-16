CREATE TABLE taxi
(
    id            bigserial PRIMARY KEY,
    name          character varying(32)      NOT NULL,
    latitude      double precision,
    longitude     double precision,
    status        character varying(10) NOT NULL,
    registered_on timestamp(0)  NOT NULL
)