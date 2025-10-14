package com.ertb.services;

import com.ertb.enumerations.EventStatus;
import com.ertb.mappers.EventMapper;
import com.ertb.model.EventModel;
import com.ertb.model.entities.Event;
import com.ertb.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;

    private final EventRepository eventRepository;


    public EventModel createEvent(EventModel eventModel) {

        if (eventModel.getEndDate().isBefore(eventModel.getStartDate())) {
            throw new RuntimeException("The end date time must be before the start date.");
        }

        Event event = eventMapper.eventModelToEvent(eventModel);

        if (event.getStartDate().equals(LocalDate.now())) {
            if(event.getStartTime().isBefore(LocalTime.now()) || event.getStartTime().equals(LocalTime.now())) {
                event.setEventStatus(EventStatus.OPEN);
            }
        }

        event.setEventStatus(EventStatus.UPCOMING);
        event = eventRepository.save(event);
        EventModel returnEventModel = eventMapper.eventToEventModel(event);

        return returnEventModel;
    }



    @Scheduled(cron = "0 0/30 * * * ?")  //Every Day, Every 30 minutes
    public void startEvent() {
        log.info("Event start method is triggered at "+ LocalDateTime.now());
        List<Event> eventList = eventRepository.findByEventStatusAndStartDate(EventStatus.UPCOMING, LocalDate.now());
        eventList.forEach(event -> {
            if(event.getStartTime().isBefore(LocalTime.now()) || event.getStartTime().equals(LocalTime.now())) {
                event.setEventStatus(EventStatus.OPEN);
                eventRepository.save(event);
            }
            log.info("Event Status updated");
        });
    }



    @Scheduled(cron = "0 0/30 * * * ?")  //Every Day, Every 30 minutes
    public void endEvent() {
        log.info("Event end method is triggered at "+ LocalDateTime.now());
        List<Event> eventList = eventRepository.findByEventStatusAndEndDate(EventStatus.OPEN, LocalDate.now());
        eventList.forEach(event -> {
            if(event.getEndTime().isBefore(LocalTime.now()) || event.getEndTime().equals(LocalTime.now())) {
                event.setEventStatus(EventStatus.CLOSED);
                eventRepository.save(event);
            }
            log.info("Event Status updated");
        });
    }



    public List<EventModel> getAllEvent() {

        List<Event> eventList = eventRepository.findAll();

        List<EventModel> eventModelList = eventMapper.eventListToEventModelList(eventList);

        return eventModelList;
    }
}
