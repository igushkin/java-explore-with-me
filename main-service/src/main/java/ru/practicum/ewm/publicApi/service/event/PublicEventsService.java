package ru.practicum.ewm.publicApi.service.event;

import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.EventShortDto;
import ru.practicum.ewm.publicApi.dto.RequestParamForEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface PublicEventsService {

    Set<EventShortDto> getAll(RequestParamForEvent param);

    EventFullDto get(Long id, HttpServletRequest request);
}
