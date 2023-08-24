package ru.practicum.ewm.public_access.service.event;

import ru.practicum.ewm.common.dto.event.EventFullDto;
import ru.practicum.ewm.common.dto.event.EventShortDto;
import ru.practicum.ewm.public_access.dto.RequestParamForEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventsService {

    List<EventShortDto> getAll(RequestParamForEvent param);

    EventFullDto get(Long id, HttpServletRequest request);
}