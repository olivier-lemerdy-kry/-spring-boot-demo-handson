package se.kry.springboot.demo.handson.domain;

import java.time.ZonedDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record EventCreationRequest(@NotBlank String title, @NotNull ZonedDateTime start, @NotNull ZonedDateTime end) {
}
