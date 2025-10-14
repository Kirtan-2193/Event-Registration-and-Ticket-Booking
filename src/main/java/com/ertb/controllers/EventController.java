package com.ertb.controllers;

import com.ertb.model.EventModel;
import com.ertb.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventModel> addEvent(@RequestBody EventModel eventModel) {
        return ResponseEntity.ok(eventService.createEvent(eventModel));
    }

    @GetMapping
    public ResponseEntity<List<EventModel>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvent());
    }

    @GetMapping("/start-event")
    public void eventSchedulerForStart() {
        eventService.startEvent();
    }

    @GetMapping("end-event")
    public void eventSchedulerForEnd() {
        eventService.endEvent();
    }
}
