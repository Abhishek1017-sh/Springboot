package com.vms.service;

import com.vms.dto.EventDto;
import com.vms.entity.Event;
import com.vms.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setLocation(eventDto.getLocation());
        event.setDate(eventDto.getDate());
        
        event = eventRepository.save(event);
        eventDto.setId(event.getId());
        return eventDto;
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> {
            EventDto dto = new EventDto();
            dto.setId(event.getId());
            dto.setName(event.getName());
            dto.setLocation(event.getLocation());
            dto.setDate(event.getDate());
            return dto;
        }).collect(Collectors.toList());
    }
}
