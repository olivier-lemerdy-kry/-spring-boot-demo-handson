package se.kry.springboot.demo.handson.services;

import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.kry.springboot.demo.handson.data.EventRepository;
import se.kry.springboot.demo.handson.domain.EventCreationRequest;
import se.kry.springboot.demo.handson.data.Event;

@Service
public class EventService {

  private final EventRepository repository;

  public EventService(EventRepository repository) {
    this.repository = repository;
  }

  public Event createEvent(@NotNull EventCreationRequest eventCreationRequest) {
    var event = Event.from(eventCreationRequest.title(), eventCreationRequest.start(), eventCreationRequest.end());
    return repository.save(event);
  }

  public Page<Event> getEvents(@NotNull Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Optional<Event> getEvent(@NotNull UUID id) {
    return repository.findById(id);
  }

  public void deleteEvent(UUID id) {
    repository.deleteById(id);
  }
}
