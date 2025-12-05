/*
package com.ertb.registration.service;

import com.ertb.model.EventModel;
import com.ertb.model.entities.Event;
import com.ertb.repositories.EventRepository;
import com.ertb.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;  // your actual service class

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventById_Success() {
        Event event = new Event();
        when(eventRepository.findByEventId(1L));

        List<EventModel> result = eventService.getAllEvent();

        assertNotNull(result);
        assertEquals("Music Fest", result.getFirst().getEventName());
        verify(eventRepository, times(1)).findByEventId("1L");
    }

    @Test
    void testGetEventById_NotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> eventService.getEventById(99L));
    }
}
*/
