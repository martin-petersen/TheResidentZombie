package com.trz.repository;

import com.trz.model.Report;
import com.trz.model.Survivor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findByInfected(Survivor infected);

    default UUID getNextId() {
        return UUID.randomUUID();
    }
}
