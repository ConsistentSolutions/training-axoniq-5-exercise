package io.axoniq.training.trainingaxoniq5.bikerental.ui;

import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RegisterBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RequestBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindAvailableBikes;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindBike;
import io.axoniq.training.trainingaxoniq5.bikerental.query.availablebikes.Bike;
import org.axonframework.messaging.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.queryhandling.gateway.QueryGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(RentalController.class)
class RentalControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CommandGateway commandGateway;

    @MockitoBean
    private QueryGateway queryGateway;

    @Test
    void registerBike() {
        when(commandGateway.send(any(RegisterBike.class), eq(String.class)))
                .thenReturn(CompletableFuture.completedFuture("bike-1"));

        webTestClient.post().uri("/register")
                .bodyValue(new RegisterBike("Road", "Amsterdam"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("bike-1");
    }

    @Test
    void requestBike() {
        when(commandGateway.send(any(RequestBike.class), eq(String.class)))
                .thenReturn(CompletableFuture.completedFuture("trip-1"));

        webTestClient.post().uri("/request")
                .bodyValue(new RequestBike("bike-1", "member-1"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("trip-1");
    }

    @Test
    void requestBikeReturnsBadRequestOnFailure() {
        when(commandGateway.send(any(RequestBike.class), eq(String.class)))
                .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("Requested bike is not available")));

        webTestClient.post().uri("/request")
                .bodyValue(new RequestBike("bike-1", "member-1"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Requested bike is not available");
    }

    @Test
    void getBike() {
        when(queryGateway.query(any(FindBike.class), eq(Bike.class)))
                .thenReturn(CompletableFuture.completedFuture(new Bike("bike-1", "Road", "Amsterdam", true)));

        webTestClient.get().uri("/bike/bike-1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.bikeId").isEqualTo("bike-1")
                .jsonPath("$.type").isEqualTo("Road")
                .jsonPath("$.location").isEqualTo("Amsterdam")
                .jsonPath("$.available").isEqualTo(true);
    }

    @Test
    void getBikeReturnsNotFoundWhenMissing() {
        when(queryGateway.query(any(FindBike.class), eq(Bike.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        webTestClient.get().uri("/bike/unknown")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAvailableBikes() {
        when(queryGateway.queryMany(any(FindAvailableBikes.class), eq(Bike.class)))
                .thenReturn(CompletableFuture.completedFuture(List.of(
                        new Bike("bike-1", "Road", "Amsterdam", true),
                        new Bike("bike-2", "Mountain", "Amsterdam", true)
                )));

        webTestClient.get().uri("/bikes?location=Amsterdam")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].bikeId").isEqualTo("bike-1")
                .jsonPath("$[1].bikeId").isEqualTo("bike-2");
    }
}
