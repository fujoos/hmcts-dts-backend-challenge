package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.tasks.TaskService;
import uk.gov.hmcts.reform.dev.tasks.dto.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/tasks", produces = "application/json")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest req) {
        TaskResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/tasks/" + created.id())).body(created);
    }

    @GetMapping
    public List<TaskResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    public TaskResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateTaskRequest req) {
        return service.update(id, req);
    }

    @PatchMapping(value = "/{id}/status", consumes = "application/json")
    public TaskResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateTaskStatusRequest req) {
        return service.updateStatus(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
