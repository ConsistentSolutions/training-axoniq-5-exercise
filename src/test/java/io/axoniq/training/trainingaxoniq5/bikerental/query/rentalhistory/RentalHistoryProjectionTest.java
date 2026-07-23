package io.axoniq.training.trainingaxoniq5.bikerental.query.rentalhistory;

import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRequested;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeReturned;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindRentalHistory;
import org.axonframework.extension.springboot.test.AxonSpringBootTest;
import org.axonframework.messaging.eventhandling.gateway.EventGateway;
import org.axonframework.messaging.queryhandling.gateway.QueryGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Exercise 3 — spec for the rental-history read model.
 *
 * <p>The projection is written from scratch, so this test drives it through the framework: it publishes
 * events and reads back through the {@link FindRentalHistory} query. It never calls the projection
 * directly, so it compiles before any handler exists — and fails (the query has no data / no handler)
 * until you implement the projection. Unique ids keep the tests independent of the shared read model.
 */
@AxonSpringBootTest(properties = "axon.axonserver.enabled=false")
class RentalHistoryProjectionTest {

    @Autowired
    private EventGateway eventGateway;

    @Autowired
    private QueryGateway queryGateway;

    @Test
    void recordsRentalWhenBikeRequested() throws Exception {
        eventGateway.publish(List.of(new BikeRequested("bike-hist-1", "member-hist-1", "trip-hist-1"))).get();

        awaitUntil(() -> !history("member-hist-1").isEmpty());

        List<RentalRecord> history = history("member-hist-1");
        assertEquals(1, history.size());
        RentalRecord record = history.getFirst();
        assertEquals("trip-hist-1", record.getTripId());
        assertEquals("member-hist-1", record.getMemberId());
        assertEquals("bike-hist-1", record.getBikeId());
        assertFalse(record.isReturned());
    }

    @Test
    void marksRentalReturnedWhenBikeReturned() throws Exception {
        eventGateway.publish(List.of(new BikeRequested("bike-hist-2", "member-hist-2", "trip-hist-2"))).get();
        awaitUntil(() -> !history("member-hist-2").isEmpty());

        eventGateway.publish(List.of(new BikeReturned("bike-hist-2"))).get();
        awaitUntil(() -> history("member-hist-2").stream().anyMatch(RentalRecord::isReturned));

        List<RentalRecord> history = history("member-hist-2");
        assertEquals(1, history.size());
        assertTrue(history.getFirst().isReturned());
    }

    private List<RentalRecord> history(String memberId) {
        try {
            return queryGateway.queryMany(new FindRentalHistory(memberId), RentalRecord.class)
                               .get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            // No query handler yet (blank projection) or nothing projected — treat as "no history".
            return List.of();
        }
    }

    private void awaitUntil(BooleanSupplier condition) {
        long deadline = System.currentTimeMillis() + 5_000;
        while (System.currentTimeMillis() < deadline) {
            if (condition.getAsBoolean()) {
                return;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
