package com.ertb.services;

import com.ertb.enumerations.EventStatus;
import com.ertb.mappers.EventMapper;
import com.ertb.model.EventModel;
import com.ertb.model.entities.Event;
import com.ertb.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;

    private final EventRepository eventRepository;


    public EventModel createEvent(EventModel eventModel) {

        if (eventModel.getEndDateTime().isBefore(eventModel.getStartDateTime())) {
            throw new RuntimeException("The end date time must be before the start date.");
        }

        Event event = eventMapper.eventModelToEvent(eventModel);
        LocalDate startDate = event.getStartDateTime().toLocalDate();

        if (startDate.equals(LocalDate.now())) {
            event.setEventStatus(EventStatus.OPEN);
        }

        event.setEventStatus(EventStatus.UPCOMING);
        event = eventRepository.save(event);
        EventModel returnEventModel = eventMapper.eventToEventModel(event);

        return returnEventModel;
    }



    public List<EventModel> getAllEvent() {

        List<Event> eventList = eventRepository.findAll();

        List<EventModel> eventModelList = eventMapper.eventListToEventModelList(eventList);

        return eventModelList;
    }
}
