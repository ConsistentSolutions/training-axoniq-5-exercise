package io.axoniq.training.trainingaxoniq5.bikerental.write.reservation;

import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RequestBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.ReturnBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRequested;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeReturned;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;
import org.axonframework.modelling.annotation.InjectEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TripCommandHandler {

    // TODO (Exercise 2): a bike may only be rented by an *active* member.
    //   This is a Dynamic Consistency Boundary decision across two entities: the bike AND the member.
    //   1. Add a second sourced entity parameter: MemberState
    //   2. Reject the command when the member is not active
    //   Also implement the two @EventSourcingHandler methods in MemberState.
    //   Make RequestBikeMembershipTest pass without breaking TripCommandHandlerTest.
    @CommandHandler
    public String handle(RequestBike command, EventAppender eventAppender, @InjectEntity(idProperty = "bikeId") BikeState bikeState) {
        if (bikeState.isAvailable()) {
            String tripId = UUID.randomUUID().toString();
            eventAppender.append(new BikeRequested(command.bikeId(), command.memberId(), tripId));
            return tripId;
        }
        throw new IllegalStateException("Requested bike is not available");
    }

    /**
     * Exercise 1 — Return a bike.
     *
     * <p>Goal: complete the write side of returning a bike so a rented bike becomes available again.
     *
     * <p>What to implement:
     * <ol>
     *   <li>Here: if the injected {@link BikeState} shows the bike is currently rented, append a
     *       {@link BikeReturned} event; otherwise reject with
     *       {@code new IllegalStateException("Bike is not currently rented")}.</li>
     *   <li>In {@link BikeState}: add an {@code @EventSourcingHandler} for {@link BikeReturned} that flips
     *       the bike back to available.</li>
     *   <li>In {@code AvailableBikesProjection}: handle {@link BikeReturned} so the read model shows the
     *       bike as available again.</li>
     * </ol>
     *
     * Make the return-bike tests in {@code TripCommandHandlerTest} pass.
     */
    @CommandHandler
    public void handle(ReturnBike command, EventAppender eventAppender, @InjectEntity(idProperty = "bikeId") BikeState bikeState) {
        // TODO (Exercise 1): replace the line below with the real implementation described above.
        throw new UnsupportedOperationException("TODO Exercise 1: implement ReturnBike handling");
    }
}
