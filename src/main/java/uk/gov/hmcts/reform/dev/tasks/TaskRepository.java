package uk.gov.hmcts.reform.dev.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> { }
