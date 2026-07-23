package io.axoniq.training.trainingaxoniq5.bikerental.query.rentalhistory;

import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRequested;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeReturned;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindRentalHistory;
import org.axonframework.messaging.eventhandling.annotation.EventHandler;
import org.axonframework.messaging.queryhandling.annotation.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Exercise 3 — a second read model: each member's rental history.
 *
 * <p>Build this projection from scratch. It keeps its own JPA-backed model ({@link RentalRecord}) fed
 * from the event stream, independently of {@code AvailableBikesProjection} — a different question needs
 * a different read model. The {@code repository} is injected for you; add whatever finder methods you
 * need to {@link RentalRecordRepository}.
 *
 * <p>What to implement (make {@code RentalHistoryProjectionTest} pass):
 * <ul>
 *   <li>An {@code @EventHandler} for {@link BikeRequested} that persists a new {@link RentalRecord}
 *       (tripId, memberId, bikeId, not yet returned).</li>
 *   <li>An {@code @EventHandler} for {@link BikeReturned} that finds the still-open rental for that bike
 *       and marks it returned — this needs a new finder on {@link RentalRecordRepository}.</li>
 *   <li>A {@code @QueryHandler} for {@link FindRentalHistory} that returns all rentals for the member.</li>
 * </ul>
 *
 * <p>Note: {@link BikeRequested} carries the {@code memberId} once Exercise 2 has evolved it, and
 * {@link BikeReturned} exists once Exercise 1 is done — so tackle this exercise after those two.
 */
@Component
public class RentalHistoryProjection {

    private final RentalRecordRepository repository;

    public RentalHistoryProjection(RentalRecordRepository repository) {
        this.repository = repository;
    }

}
