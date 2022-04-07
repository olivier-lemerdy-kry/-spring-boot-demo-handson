package se.kry.springboot.demo.handson.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.kry.springboot.demo.handson.data.Event;
import se.kry.springboot.demo.handson.services.EventService;

@WebMvcTest(EventsController.class)
class EventsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private EventService service;

  @Test
  void create_event() throws Exception {
    var start = LocalDate.of(2001, Month.JANUARY, 1).atTime(LocalTime.MIDNIGHT);
    var end = start.plusHours(12);

    when(service.createEvent(any())).thenReturn(
        new Event().setTitle("Some event").setStart(start).setEnd(end));

    var payload = objectMapper.createObjectNode()
        .put("title", "someEvent")
        .put("start", "2001-01-01T00:00:00")
        .put("end", "2001-01-01T00:00:00")
        .toString();

    mockMvc.perform(post("/api/v1/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.title", is("Some event")),
            jsonPath("$.start", is("2001-01-01T00:00:00")),
            jsonPath("$.end", is("2001-01-01T12:00:00"))
        );
  }

  @Test
  void create_event_with_blank_title() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("title", " ")
        .put("start", "2001-01-01T00:00:00")
        .put("end", "2001-01-01T00:00:00")
        .toString();

    mockMvc.perform(post("/api/v1/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_event_with_too_long_title() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("title", "X".repeat(300))
        .put("start", "2001-01-01T00:00:00")
        .put("end", "2001-01-01T12:00:00")
        .toString();

    mockMvc.perform(post("/api/v1/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_event_with_null_start() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("title", "Some event")
        .put("end", "2001-01-01T00:00:00")
        .toString();

    mockMvc.perform(post("/api/v1/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_event_with_null_end() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("title", "Some event")
        .put("start", "2001-01-01T00:00:00")
        .toString();

    mockMvc.perform(post("/api/v1/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_event_with_start_after_end() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("title", "Some event")
        .put("start", "2001-01-01T12:00:00")
        .put("end", "2001-01-01T00:00:00")
        .toString();

    mockMvc.perform(post("/api/v1/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void read_events() throws Exception {
    mockMvc.perform(get("/api/v1/events"))
        .andExpect(status().isOk());
  }

  @Test
  void read_event() throws Exception {
    var uuid = UUID.fromString("38a14a82-d5a2-4210-9d61-cc3577bfa5df");

    var start = LocalDate.of(2001, Month.JANUARY, 1).atTime(LocalTime.MIDNIGHT);
    var end = start.plusHours(12);

    when(service.getEvent(uuid)).thenReturn(Optional.of(new Event().setTitle("Some event").setStart(start).setEnd(end)));

    mockMvc.perform(get("/api/v1/events/{id}", uuid))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Some event")))
        .andExpect(jsonPath("$.start", is("2001-01-01T00:00:00")))
        .andExpect(jsonPath("$.end", is("2001-01-01T12:00:00")));
  }

  @Test
  void update_event() throws Exception {
    var uuid = UUID.fromString("38a14a82-d5a2-4210-9d61-cc3577bfa5df");

    var payload = objectMapper.createObjectNode()
        .put("title", "Some other event")
        .toString();

    mockMvc.perform(patch("/api/v1/events/{id}", uuid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isOk());
  }

  @Test
  void update_event_with_incorrect_id() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("title", "Some other event")
        .toString();

    mockMvc.perform(patch("/api/v1/events/{id}", "foobar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_event_with_too_long_title() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("title", "X".repeat(300))
        .toString();

    mockMvc.perform(patch("/api/v1/events/{id}", "foobar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void update_event_with_start_after_end() throws Exception {
    var payload = objectMapper.createObjectNode()
        .put("start", "2001-01-01T12:00:00")
        .put("end", "2001-01-01T00:00:00")
        .toString();

    mockMvc.perform(patch("/api/v1/events/{id}", "foobar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isBadRequest());
  }

  @Test
  void delete_event() throws Exception {
    var uuid = UUID.fromString("38a14a82-d5a2-4210-9d61-cc3577bfa5df");

    mockMvc.perform(delete("/api/v1/events/{id}", uuid))
        .andExpect(status().isOk());
  }

  @Test
  void delete_event_with_incorrect_id() throws Exception {
    mockMvc.perform(delete("/api/v1/events/{id}", "foobar"))
        .andExpect(status().isBadRequest());
  }
}