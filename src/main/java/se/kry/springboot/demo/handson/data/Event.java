package se.kry.springboot.demo.handson.data;

import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Accessors(chain = true)
@Getter
@Setter
public class Event extends AbstractPersistable<UUID> {

  @NotBlank
  private String title;

  @NotNull
  private ZonedDateTime start;

  @NotNull
  private ZonedDateTime end;
}
