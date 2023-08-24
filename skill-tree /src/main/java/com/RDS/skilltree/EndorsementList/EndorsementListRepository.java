package com.RDS.skilltree.EndorsementList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EndorsementListRepository extends JpaRepository<EndorsementListModel, UUID> {
}
