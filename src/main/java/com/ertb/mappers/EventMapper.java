package com.ertb.mappers;

import com.ertb.model.BookedEvent;
import com.ertb.model.EventModel;
import com.ertb.model.entities.Event;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true),
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface EventMapper {

    Event eventModelToEvent(EventModel eventModel);

    EventModel eventToEventModel(Event event);

    List<EventModel> eventListToEventModelList(List<Event> eventList);

    BookedEvent eventToBookedEvent(Event event);

    List<BookedEvent> eventListToBookedEventList(List<Event> eventList);
}
