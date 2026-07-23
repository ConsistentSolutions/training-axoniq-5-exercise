package io.axoniq.training.trainingaxoniq5.bikerental.query.rentalhistory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Read model for Exercise 3 — one row per rental (a bike requested by a member).
 * {@code returned} flips to {@code true} once the bike is handed back.
 */
@Entity
public class RentalRecord {

    @Id
    private String tripId;
    private String memberId;
    private String bikeId;
    private boolean returned;

    protected RentalRecord() {
    }

    public RentalRecord(String tripId, String memberId, String bikeId, boolean returned) {
        this.tripId = tripId;
        this.memberId = memberId;
        this.bikeId = bikeId;
        this.returned = returned;
    }

    public String getTripId() { return tripId; }
    public String getMemberId() { return memberId; }
    public String getBikeId() { return bikeId; }
    public boolean isReturned() { return returned; }

    public void setReturned(boolean returned) { this.returned = returned; }
}
