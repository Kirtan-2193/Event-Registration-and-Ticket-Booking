package com.ertb.mappers;

import com.ertb.model.TicketModel;
import com.ertb.model.entities.Ticket;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        builder = @Builder(disableBuilder = true),
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface TicketMapper {

    TicketModel ticketToTicketModel(Ticket ticket);
}
