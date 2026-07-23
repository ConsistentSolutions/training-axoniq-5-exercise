package io.axoniq.training.trainingaxoniq5.bikerental.write.membership;

import io.axoniq.training.trainingaxoniq5.bikerental.api.event.MemberRegistered;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.MemberSuspended;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator;
import org.axonframework.extension.spring.stereotype.EventSourced;

/**
 * Exercise 2 — Member decision model (second event-sourced entity).
 *
 * <p>This entity is sourced from the events tagged {@code memberId} and is used by
 * {@code TripCommandHandler} to decide whether a member may rent a bike (see that class).
 *
 * <p>What to implement: the {@code @EventSourcingHandler} methods below so that {@link #isActive()}
 * returns {@code true} only for a registered, non-suspended member. A member that has never been
 * registered is created by {@link #MemberState()} and must be considered inactive.
 */
@EventSourced(tagKey = "memberId")
public class MemberState {

    private boolean active;

    @EntityCreator
    public MemberState() {
    }

    public boolean isActive() {
        return active;
    }
}
