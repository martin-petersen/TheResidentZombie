package com.trz.repository;

import com.trz.model.Survivor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SurvivorRepository extends JpaRepository<Survivor, UUID> {

    List<Survivor> findByInfected(boolean infected);

    default UUID getNextId() {
        return UUID.randomUUID();
    }

    long countAllByInfected(boolean infected);
}
