package uk.gov.hmcts.reform.dev.tasks.dto;

import uk.gov.hmcts.reform.dev.tasks.TaskStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    OffsetDateTime dueDateTime,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) { }
