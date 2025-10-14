package com.ertb.repositories;

import com.ertb.enumerations.EventStatus;
import com.ertb.model.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    Optional<Event> findByEventId(String eventId);

    List<Event> findByEventStatusAndStartDate(EventStatus eventStatus, LocalDate startDate);
    List<Event> findByEventStatusAndEndDate(EventStatus eventStatus, LocalDate endDate);
}
