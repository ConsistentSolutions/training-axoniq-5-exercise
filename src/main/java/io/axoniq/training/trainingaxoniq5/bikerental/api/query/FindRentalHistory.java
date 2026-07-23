package io.axoniq.training.trainingaxoniq5.bikerental.api.query;

import org.axonframework.messaging.queryhandling.annotation.Query;

@Query(name = "findRentalHistory")
public record FindRentalHistory(String memberId) {
}
