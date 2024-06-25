package com.RDS.skilltree.User;

import com.RDS.skilltree.utils.TrackedProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@JsonSerialize
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserModel extends TrackedProperties {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "rds_user_id", unique = true)
    private String rdsUserId;
}
