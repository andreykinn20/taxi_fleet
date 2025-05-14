CREATE TABLE taxi
(
    id                    bigserial PRIMARY KEY,
    owner_id              integer      NOT NULL,
    origin_latitude       double precision,
    origin_longitude      double precision,
    destination_latitude  double precision,
    destination_longitude double precision,
    status                varying(10)      NOT NULL,
    registered_on         timestamp(0) NOT NULL,
)