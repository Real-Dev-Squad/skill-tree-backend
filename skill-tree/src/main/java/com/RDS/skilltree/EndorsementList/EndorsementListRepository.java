package com.RDS.skilltree.EndorsementList;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndorsementListRepository extends JpaRepository<EndorsementListModel, UUID> {}
