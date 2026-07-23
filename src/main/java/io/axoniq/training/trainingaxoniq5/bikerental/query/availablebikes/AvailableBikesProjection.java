package io.axoniq.training.trainingaxoniq5.bikerental.query.availablebikes;

import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRegistered;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRequested;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindAvailableBikes;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindBike;
import org.axonframework.messaging.eventhandling.annotation.EventHandler;
import org.axonframework.messaging.queryhandling.annotation.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AvailableBikesProjection {

    private final BikeRepository bikeRepository;

    public AvailableBikesProjection(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    @EventHandler
    public void on(BikeRegistered event) {
        bikeRepository.save(new Bike(event.bikeId(), event.bikeType(), event.location(), true));
    }

    @EventHandler
    public void on(BikeRequested event) {
        bikeRepository.findById(event.bikeId()).ifPresent(bike -> bike.setAvailable(false));
    }

    // TODO (Exercise 1): handle BikeReturned by setting the bike's availability back to true, so it
    //   reappears in the available-bikes read model.

    @QueryHandler
    public Optional<Bike> handle(FindBike query) {
        return bikeRepository.findById(query.bikeId());
    }

    @QueryHandler
    public List<Bike> on(FindAvailableBikes query) {
        return bikeRepository.findByLocationAndAvailableTrue(query.location());
    }
}
