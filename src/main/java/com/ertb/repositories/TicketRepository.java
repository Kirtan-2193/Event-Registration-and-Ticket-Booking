package com.ertb.repositories;

import com.ertb.enumerations.Status;
import com.ertb.model.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByUserUserIdAndStatus(String userId, Status status);

    Ticket findByEventEventId(String eventId);
}
