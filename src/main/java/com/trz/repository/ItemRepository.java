package com.trz.repository;

import com.trz.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    default UUID getNextId() {
        return UUID.randomUUID();
    }
}
