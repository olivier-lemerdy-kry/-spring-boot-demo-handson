package se.kry.springboot.demo.handson.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  @MockBean
  private EventService service;

  @Test
  void create_event() throws Exception {
    var start = LocalDate.of(2001, Month.JANUARY, 1).atTime(LocalTime.MIDNIGHT);
    var end = start.plusHours(12);

    when(service.createEvent(any())).thenReturn(
        new Event().setTitle("Some event").setStart(start).setEnd(end));


    mockMvc.perform(post("/api/v1/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"Some event\",\"start\":\"2001-01-01T00:00:00\",\"end\":\"2001-01-01T00:00:00\"}"))
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.title", is("Some event")),
            jsonPath("$.start", is("2001-01-01T00:00:00")),
            jsonPath("$.end", is("2001-01-01T12:00:00"))
        );
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
  void delete_event() throws Exception {
    var uuid = UUID.fromString("38a14a82-d5a2-4210-9d61-cc3577bfa5df");

    mockMvc.perform(delete("/api/v1/events/{id}", uuid))
        .andExpect(status().isOk());
  }
}