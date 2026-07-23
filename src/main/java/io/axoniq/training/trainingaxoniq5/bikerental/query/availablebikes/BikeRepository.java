package io.axoniq.training.trainingaxoniq5.bikerental.query.availablebikes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BikeRepository extends JpaRepository<Bike, String> {

    List<Bike> findByLocationAndAvailableTrue(String location);
}
