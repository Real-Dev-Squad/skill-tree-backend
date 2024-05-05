package com.RDS.skilltree.utils;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class TrackedProperties {
    @ManyToOne
    @JoinColumn(name = "created_by")
    private UUID createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
