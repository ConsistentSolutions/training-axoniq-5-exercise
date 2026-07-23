package io.axoniq.training.trainingaxoniq5.bikerental.api.event;

import org.axonframework.eventsourcing.annotation.EventTag;
import org.axonframework.messaging.eventhandling.annotation.Event;

@Event
public record BikeRequested(@EventTag String bikeId, String memberId, @EventTag String tripId) {
}
