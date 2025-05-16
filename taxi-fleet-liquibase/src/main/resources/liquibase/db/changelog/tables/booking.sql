CREATE TABLE booking
(
    id                    bigserial PRIMARY KEY,
    user_id               integer          NOT NULL,
    taxi_id               integer REFERENCES taxi (id),
    origin_latitude       double precision NOT NULL,
    origin_longitude      double precision NOT NULL,
    destination_latitude  double precision NOT NULL,
    destination_longitude double precision NOT NULL,
    status                character varying(10)    NOT NULL,
    created_on            timestamp(3)     NOT NULL,
    updated_on            timestamp(3),
    version               integer          NOT NULL
);

CREATE INDEX booking_status ON booking (status);