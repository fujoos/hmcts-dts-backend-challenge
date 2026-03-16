package uk.gov.hmcts.reform.dev;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.tasks.TaskStatus;
import uk.gov.hmcts.reform.dev.tasks.dto.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.tasks.dto.TaskResponse;
import uk.gov.hmcts.reform.dev.tasks.dto.UpdateTaskStatusRequest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    void create_get_delete_roundTrip() throws Exception {
        CreateTaskRequest req = new CreateTaskRequest(
            "My task",
            "Optional description",
            TaskStatus.TODO,
            OffsetDateTime.now(ZoneOffset.UTC).plusDays(1)
        );

        var createResult = mvc.perform(post("/tasks")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn();

        TaskResponse created = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            TaskResponse.class
        );

        assertThat(created.id()).isNotNull();

        mvc.perform(get("/tasks/" + created.id()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("My task"));

        mvc.perform(delete("/tasks/" + created.id()))
            .andExpect(status().isNoContent());

        mvc.perform(get("/tasks/" + created.id()))
            .andExpect(status().isNotFound());
    }

    @Test
    void validation_error_on_missing_title() throws Exception {
        String body = """
          {"title":"","description":"x","status":"TODO","dueDateTime":"2030-01-01T10:00:00Z"}
          """;

        mvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Validation failed"))
            .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void update_status() throws Exception {
        CreateTaskRequest req = new CreateTaskRequest(
            "Status task",
            null,
            TaskStatus.TODO,
            OffsetDateTime.now(ZoneOffset.UTC).plusDays(2)
        );

        var createResult = mvc.perform(post("/tasks")
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andReturn();

        TaskResponse created = objectMapper.readValue(
            createResult.getResponse().getContentAsString(),
            TaskResponse.class
        );

        UpdateTaskStatusRequest patch = new UpdateTaskStatusRequest(TaskStatus.DONE);

        mvc.perform(patch("/tasks/" + created.id() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DONE"));
    }
}
