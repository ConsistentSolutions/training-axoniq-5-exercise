package io.axoniq.training.trainingaxoniq5.bikerental.write.reservation;

import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRegistered;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRequested;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator;
import org.axonframework.extension.spring.stereotype.EventSourced;

@EventSourced(tagKey = "bikeId")
public class BikeState {

    private boolean reserved;

    @EntityCreator
    public BikeState() {
    }

    @EventSourcingHandler
    public void on(BikeRegistered event) {
        reserved = false;
    }

    @EventSourcingHandler
    public void on(BikeRequested event) {
        reserved = true;
    }

    // TODO (Exercise 1): add an @EventSourcingHandler for BikeReturned that makes the bike available
    //   again (reserved = false), so a returned bike can be requested once more.

    public boolean isAvailable() {
        return !reserved;
    }
}
