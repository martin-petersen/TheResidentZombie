package com.trz.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Report {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "infected_id")
    private Survivor infected;
    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Survivor reporter;

    public Report() {
    }

    public Report(UUID id, Survivor infected, Survivor reporter) {
        this.id = id;
        this.infected = infected;
        this.reporter = reporter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id.equals(report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Survivor getInfected() {
        return infected;
    }

    public void setInfected(Survivor infected) {
        this.infected = infected;
    }

    public Survivor getReporter() {
        return reporter;
    }

    public void setReporter(Survivor reporter) {
        this.reporter = reporter;
    }
}
