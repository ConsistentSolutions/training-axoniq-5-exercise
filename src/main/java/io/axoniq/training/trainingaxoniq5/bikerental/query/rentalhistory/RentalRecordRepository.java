package io.axoniq.training.trainingaxoniq5.bikerental.query.rentalhistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRecordRepository extends JpaRepository<RentalRecord, String> {

    List<RentalRecord> findByMemberId(String memberId);
}
