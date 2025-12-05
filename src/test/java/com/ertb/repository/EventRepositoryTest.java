/*
package com.ertb.registration.repository;

import com.ertb.model.entities.Event;
import com.ertb.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void testSaveAndFindEvent() {
        Event event = new Event();
        eventRepository.save(event);

        List<Event> events = eventRepository.findAll();
        assertThat(events).isNotEmpty();
        assertThat(events.get(0).getEventName()).isEqualTo("Cultural Fest");
    }
}
*/
