package com.nand.grievance.publisher.repository;

import com.nand.grievance.publisher.entity.EscalationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalationRepository extends JpaRepository<EscalationEntity, Long> {

}
