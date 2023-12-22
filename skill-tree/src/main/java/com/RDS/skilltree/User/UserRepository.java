package com.RDS.skilltree.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    Optional<UserModel> findByRdsUserId(String rdsUserId);

    Boolean existsByRdsUserId(String rdsUserId);
}
