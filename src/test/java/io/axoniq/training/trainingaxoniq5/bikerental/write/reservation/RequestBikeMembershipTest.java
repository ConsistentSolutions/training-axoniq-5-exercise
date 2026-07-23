package io.axoniq.training.trainingaxoniq5.bikerental.write.reservation;

import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RequestBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRegistered;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.MemberRegistered;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.MemberSuspended;
import org.axonframework.extension.springboot.test.AxonSpringBootTest;
import org.axonframework.test.fixture.AxonTestFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Exercise 2 — spec for the membership rule on {@code RequestBike}. This is a Dynamic Consistency
 * Boundary decision: the handler must source both the bike and the member.
 *
 * <p>{@link #canRequestBikeForActiveMember()} already passes against the starting code. The other two
 * fail until {@code TripCommandHandler} injects {@code MemberState} and rejects inactive members, and
 * {@code MemberState} implements its event-sourcing handlers.
 */
@AxonSpringBootTest(properties = "axon.axonserver.enabled=false")
class RequestBikeMembershipTest {

    @Autowired
    private AxonTestFixture fixture;

    @Test
    void canRequestBikeForActiveMember() {
        fixture.given()
                .event(new MemberRegistered("member-1", "John"))
                .event(new BikeRegistered("bike-1", "Road", "Berlin"))
                .when()
                .command(new RequestBike("bike-1", "member-1"))
                .then()
                .success();
    }

    @Test
    void cannotRequestBikeForSuspendedMember() {
        fixture.given()
                .event(new MemberRegistered("member-1", "John"))
                .event(new MemberSuspended("member-1"))
                .event(new BikeRegistered("bike-1", "Road", "Berlin"))
                .when()
                .command(new RequestBike("bike-1", "member-1"))
                .then()
                .exception(IllegalStateException.class, "Member is not active");
    }

    @Test
    void cannotRequestBikeForUnknownMember() {
        fixture.given()
                .event(new BikeRegistered("bike-1", "Road", "Berlin"))
                .when()
                .command(new RequestBike("bike-1", "unknown"))
                .then()
                .exception(IllegalStateException.class, "Member is not active");
    }
}
