package com.ertb.controllers;

import com.ertb.model.EventModel;
import com.ertb.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("@authService.hassPermission(T(com.ertb.enumerations.PermissionEnum).CREATE_EVENT)")
    public ResponseEntity<EventModel> addEvent(@RequestBody EventModel eventModel) {
        return ResponseEntity.ok(eventService.createEvent(eventModel));
    }

    @GetMapping
    @PreAuthorize("@authService.hassPermission(T(com.ertb.enumerations.PermissionEnum).VIEW_ALL_EVENTS)")
    public ResponseEntity<List<EventModel>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvent());
    }


    @PutMapping
    @PreAuthorize("@authService.hassPermission(T(com.ertb.enumerations.PermissionEnum).UPDATE_EVENT)")
    public ResponseEntity<EventModel> updateEvent(@RequestBody EventModel eventModel,
                                                  @RequestParam String eventId) {
        return ResponseEntity.ok(eventService.updateEvent(eventModel, eventId));
    }

    /*@GetMapping("/start-event")
    public void eventSchedulerForStart() {
        eventService.startEvent();
    }

    @GetMapping("end-event")
    public void eventSchedulerForEnd() {
        eventService.endEvent();
    }*/
}
