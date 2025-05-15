CREATE TABLE taxi
(
    id            bigserial PRIMARY KEY,
    owner_id      integer       NOT NULL,
    latitude      double precision,
    longitude     double precision,
    status        character(10) NOT NULL,
    registered_on timestamp(0)  NOT NULL
)