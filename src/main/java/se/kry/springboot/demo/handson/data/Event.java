package se.kry.springboot.demo.handson.data;

import static java.util.Objects.requireNonNull;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Event extends AbstractPersistable<UUID> {

  @NotBlank
  private String title;

  @NotNull
  private ZonedDateTime start;

  @NotNull
  private ZonedDateTime end;

  public static Event from(@NotBlank String title, @NotNull ZonedDateTime start, @NotNull ZonedDateTime end) {
    var event = new Event();
    event.title = requireNonNull(title);
    event.start = requireNonNull(start);
    event.end = requireNonNull(end);
    return event;
  }

  public String getTitle() {
    return title;
  }

  public ZonedDateTime getStart() {
    return start;
  }

  public ZonedDateTime getEnd() {
    return end;
  }
}
