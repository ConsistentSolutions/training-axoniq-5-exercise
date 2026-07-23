package io.axoniq.training.trainingaxoniq5.bikerental.query.availablebikes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Bike {

    @Id
    private String bikeId;
    private String type;
    private String location;
    private boolean available;

    protected Bike() {}

    public Bike(String bikeId, String type, String location, boolean available) {
        this.bikeId = bikeId;
        this.type = type;
        this.location = location;
        this.available = available;
    }

    public String getBikeId() { return bikeId; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public boolean isAvailable() { return available; }

    public void setLocation(String location) { this.location = location; }
    public void setAvailable(boolean available) { this.available = available; }
}
