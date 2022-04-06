package se.kry.springboot.demo.handson.domain;

import java.time.ZonedDateTime;
import java.util.Optional;

public record EventUpdateRequest(Optional<String> title, Optional<ZonedDateTime> start, Optional<ZonedDateTime> end) {
}
