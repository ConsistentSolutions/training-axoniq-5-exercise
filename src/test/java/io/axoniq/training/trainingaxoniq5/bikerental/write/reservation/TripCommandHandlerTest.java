package io.axoniq.training.trainingaxoniq5.bikerental.write.reservation;

import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RequestBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.ReturnBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRegistered;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRequested;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeReturned;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.MemberRegistered;
import org.axonframework.extension.springboot.test.AxonSpringBootTest;
import org.axonframework.test.fixture.AxonTestFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AxonSpringBootTest(properties = "axon.axonserver.enabled=false")
class TripCommandHandlerTest {

    @Autowired
    private AxonTestFixture fixture;

    @Test
    void testCanRequestBike() {
        fixture.given()
                .event(new MemberRegistered("member-1", "John"))
                .event(new BikeRegistered("1", "Road", "Berlin"))
                .when()
                .command(new RequestBike("1", "member-1"))
                .then()
                .success()
                .eventsSatisfy(events -> {
                    assertEquals(1, events.size());
                    assertEquals(BikeRequested.class, events.getFirst().payloadType());
                    BikeRequested eventMessage = events.getFirst().payloadAs(BikeRequested.class);
                    assertEquals("1", eventMessage.bikeId());
                    assertEquals("member-1", eventMessage.memberId());
                });
    }

    @Test
    void testCannotRequestAlreadyReservedBike() {
        fixture.given()
                .event(new MemberRegistered("member-1", "John"))
                .event(new MemberRegistered("member-2", "Jane"))
                .event(new BikeRegistered("1", "Road", "Berlin"))
                .event(new BikeRequested("1", "member-1", "trip-1"))
                .when()
                .command(new RequestBike("1", "member-2"))
                .then()
                .exception(IllegalStateException.class, "Requested bike is not available");
    }

    // Exercise 1 — returning a bike. These fail until the ReturnBike handler in TripCommandHandler and
    // the BikeReturned handling in BikeState are implemented.

    @Test
    void canReturnRentedBike() {
        fixture.given()
                .event(new BikeRegistered("1", "Road", "Berlin"))
                .event(new BikeRequested("1", "member-1", "trip-1"))
                .when()
                .command(new ReturnBike("1"))
                .then()
                .success()
                .eventsSatisfy(events -> {
                    assertEquals(1, events.size());
                    assertEquals(BikeReturned.class, events.getFirst().payloadType());
                    assertEquals("1", events.getFirst().payloadAs(BikeReturned.class).bikeId());
                });
    }

    @Test
    void cannotReturnAvailableBike() {
        fixture.given()
                .event(new BikeRegistered("1", "Road", "Berlin"))
                .when()
                .command(new ReturnBike("1"))
                .then()
                .exception(IllegalStateException.class, "Bike is not currently rented");
    }

    @Test
    void bikeCanBeRequestedAgainAfterReturn() {
        fixture.given()
                .event(new MemberRegistered("member-1", "John"))
                .event(new BikeRegistered("1", "Road", "Berlin"))
                .event(new BikeRequested("1", "member-1", "trip-1"))
                .event(new BikeReturned("1"))
                .when()
                .command(new RequestBike("1", "member-1"))
                .then()
                .success();
    }

}
