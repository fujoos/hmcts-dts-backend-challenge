package uk.gov.hmcts.reform.dev.tasks;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.dev.tasks.dto.*;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TaskResponse create(CreateTaskRequest req) {
        TaskEntity entity = new TaskEntity(
            UUID.randomUUID(),
            req.title(),
            req.description(),
            req.status(),
            req.dueDateTime()
        );
        TaskEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(UUID id) {
        TaskEntity entity = repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "dueDateTime"))
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public TaskResponse update(UUID id, UpdateTaskRequest req) {
        TaskEntity entity = repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

        entity.setTitle(req.title());
        entity.setDescription(req.description());
        entity.setStatus(req.status());
        entity.setDueDateTime(req.dueDateTime());

        TaskEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional
    public TaskResponse updateStatus(UUID id, UpdateTaskStatusRequest req) {
        TaskEntity entity = repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

        entity.setStatus(req.status());

        TaskEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private TaskResponse toResponse(TaskEntity e) {
        return new TaskResponse(
            e.getId(),
            e.getTitle(),
            e.getDescription(),
            e.getStatus(),
            e.getDueDateTime(),
            e.getCreatedAt(),
            e.getUpdatedAt()
        );
    }
}
