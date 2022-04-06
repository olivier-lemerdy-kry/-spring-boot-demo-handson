package se.kry.springboot.demo.handson.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class EventUpdateRequestJsonTest {

  @Autowired
  private JacksonTester<EventUpdateRequest> jacksonTester;

  @Test
  void deserialize_empty() throws IOException {
    var eventUpdateRequest = jacksonTester.parseObject("{}");

    assertThat(eventUpdateRequest).isNotNull();
    assertThat(eventUpdateRequest.title()).isEmpty();
    assertThat(eventUpdateRequest.start()).isEmpty();
    assertThat(eventUpdateRequest.end()).isEmpty();
  }

  @Test
  void deserialize() throws Exception {
    var zoneId = ZoneId.of("UTC");
    var start = LocalDate.of(2001, Month.JANUARY, 1)
        .atTime(LocalTime.MIDNIGHT)
        .atZone(zoneId);
    var end = LocalDate.of(2001, Month.JANUARY, 1)
        .atTime(LocalTime.NOON)
        .atZone(zoneId);

    var eventUpdateRequest = jacksonTester.readObject("EventUpdateRequest.json");

    assertThat(eventUpdateRequest).isNotNull();
    assertThat(eventUpdateRequest.title()).hasValue("Some event");
    assertThat(eventUpdateRequest.start()).hasValue(start);
    assertThat(eventUpdateRequest.end()).hasValue(end);
  }

}