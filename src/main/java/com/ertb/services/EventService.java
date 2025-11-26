package com.ertb.services;

import com.ertb.enumerations.EventStatus;
import com.ertb.exceptions.DataNotFoundException;
import com.ertb.exceptions.DataValidationException;
import com.ertb.mappers.EventMapper;
import com.ertb.model.EventModel;
import com.ertb.model.entities.Event;
import com.ertb.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    private final RedisTemplate<String, Object> redisTemplate;

    private final String EVENT_KEY_PREFIX = "EVENT_";
    private final long EVENT_TTL_SECONDS = 2 * 24 * 60 * 60; // 2 days


    public EventModel createEvent(EventModel eventModel) {

        if (eventModel.getEndDate().isBefore(eventModel.getStartDate())) {
            throw new DataValidationException("The end date time must be before the start date.");
        }

        if (eventModel.getAvailableTicket() <= 0) {
            throw new DataValidationException("Available ticket must be greater than 0.");
        }

        Event event = eventMapper.eventModelToEvent(eventModel);

        if (event.getStartDate().equals(LocalDate.now())) {
            if (event.getStartTime().isBefore(LocalTime.now()) ||
                    event.getStartTime().equals(LocalTime.now())) {
                event.setEventStatus(EventStatus.OPEN);
            }
        }

        event.setEventStatus(EventStatus.UPCOMING);
        event = eventRepository.save(event);

        EventModel returnEventModel = eventMapper.eventToEventModel(event);

        // Save event in Redis for 2 days
        String redisKey = EVENT_KEY_PREFIX + event.getEventId();
        redisTemplate.opsForValue().set(redisKey, returnEventModel, Duration.ofDays(2));

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

        String redisKey = "ALL_EVENTS";

        // Check if data exists in Redis
        List<EventModel> cachedEventList = (List<EventModel>) redisTemplate.opsForValue().get(redisKey);

        if (cachedEventList != null) {
            log.info("Fetching Events from Redis Cache");
            return cachedEventList;
        }

        // Redis miss → Load from DB
        List<Event> eventList = eventRepository.findAll();
        List<EventModel> eventModelList = eventMapper.eventListToEventModelList(eventList);

        // Save in Redis for 2 days
        redisTemplate.opsForValue().set(redisKey, eventModelList, Duration.ofDays(2));

        return eventModelList;
    }




    public EventModel updateEvent(EventModel eventModel, String eventId) {
        Event event = eventRepository.findByEventId(eventId).orElseThrow(
                () -> new DataNotFoundException("Event Not found on this Id:- "+eventId));

        eventMapper.updateEventFromEventModel(eventModel, event);
        event.setEventId(eventId);
        Event saveEvent = eventRepository.save(event);

        return eventMapper.eventToEventModel(saveEvent);
    }
}
