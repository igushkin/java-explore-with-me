package ru.practicum.ewm.public_access.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.compilation.CompilationDto;
import ru.practicum.ewm.common.entity.Compilation;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.CompilationMapper;
import ru.practicum.ewm.common.repository.CompilationRepository;
import ru.practicum.ewm.common.util.MyPageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        MyPageRequest pageable = new MyPageRequest(from, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).toList();
        }

        log.info("Get list compilations:");
        return CompilationMapper.toDtoList(compilations);
    }

    @Override
    public CompilationDto get(Long comId) {
        final Compilation compilation = compilationRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation not found with id = %s", comId)));
        log.info("Get compilation: {}", compilation.getTitle());
        return CompilationMapper.toDto(compilation);
    }
}