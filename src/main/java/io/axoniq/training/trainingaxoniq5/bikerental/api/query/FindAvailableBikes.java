package io.axoniq.training.trainingaxoniq5.bikerental.api.query;

import org.axonframework.messaging.queryhandling.annotation.Query;

@Query(namespace = "availableBikes", name = "findAvailableBikes", version = "1.0")
public record FindAvailableBikes(String location) {
}
