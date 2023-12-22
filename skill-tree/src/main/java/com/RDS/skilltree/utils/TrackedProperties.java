package com.RDS.skilltree.utils;

import com.RDS.skilltree.User.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import java.time.Instant;

@Data
@MappedSuperclass
public abstract class TrackedProperties {
    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserModel createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserModel updatedBy;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
