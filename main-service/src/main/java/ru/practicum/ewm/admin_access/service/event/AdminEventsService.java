package ru.practicum.ewm.admin_access.service.event;

import ru.practicum.ewm.admin_access.dto.RequestParamForEvent;
import ru.practicum.ewm.common.dto.event.EventFullDto;
import ru.practicum.ewm.common.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventsService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamForEvent param);
}