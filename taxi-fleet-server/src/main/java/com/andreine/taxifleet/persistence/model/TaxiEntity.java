package com.andreine.taxifleet.persistence.model;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taxi")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TaxiEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Integer ownerId;

    @Embedded
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaxiStatus status;

    @Column(nullable = false)
    private Instant registeredOn;

}
