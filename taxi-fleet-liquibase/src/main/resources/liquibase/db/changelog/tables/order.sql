CREATE TABLE order
(
    id                    bigserial PRIMARY KEY,
    user_id               integer          NOT NULL,
    taxi_id               integer REFERENCES products (taxi),
    origin_latitude       double precision NOT NULL,
    origin_longitude      double precision NOT NULL,
    destination_latitude  double precision NOT NULL,
    destination_longitude double precision NOT NULL,
    status                varying(10)      NOT NULL,
    created_on            timestamp(3)     NOT NULL,
    updated_on            timestamp(3)
);

CREATE INDEX order_status ON order (status);