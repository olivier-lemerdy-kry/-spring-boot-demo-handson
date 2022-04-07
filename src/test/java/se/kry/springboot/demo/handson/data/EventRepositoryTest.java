package se.kry.springboot.demo.handson.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class EventRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private EventRepository repository;

  @Test
  void get_event() {

  }

  @Test
  void get_events() {
    var start = LocalDate.of(2001, Month.JANUARY, 1).atTime(LocalTime.MIDNIGHT);
    IntStream.range(0, 50)
        .mapToObj(i -> new Event().setTitle("Event" + i).setStart(start.plusDays(i)).setEnd(start.plusDays(i).plusHours(12)))
        .forEach(event -> entityManager.persist(event));

    var events = repository.findAll(Pageable.ofSize(20));
    assertThat(events).hasSize(20);
    assertThat(events.getTotalElements()).isEqualTo(50);
    assertThat(events.getTotalPages()).isEqualTo(3);
    assertThat(events.getNumber()).isZero();
    assertThat(events.getNumberOfElements()).isEqualTo(20);
    assertThat(events.getSize()).isEqualTo(20);
  }

  @Test
  void save_event() {
    var start = LocalDate.of(2001, Month.JANUARY, 1).atTime(LocalTime.MIDNIGHT);
    var end = start.withHour(12);
    var event = repository.save(new Event().setTitle("Some event").setStart(start).setEnd(end));

    assertThat(event).isNotNull();
    assertThat(event.getId()).isNotNull();
    assertFalse(event.isNew());
    assertThat(event.getTitle()).isEqualTo("Some event");
    assertThat(event.getStart()).hasToString("2001-01-01T00:00");
    assertThat(event.getEnd()).hasToString("2001-01-01T12:00");
  }
}
