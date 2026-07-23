package io.axoniq.training.trainingaxoniq5.bikerental.ui;

import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RegisterBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RegisterMember;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RequestBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.ReturnBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.SuspendMember;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindAvailableBikes;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.query.FindRentalHistory;
import io.axoniq.training.trainingaxoniq5.bikerental.query.availablebikes.Bike;
import io.axoniq.training.trainingaxoniq5.bikerental.query.rentalhistory.RentalRecord;
import org.axonframework.messaging.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.queryhandling.gateway.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/")
public class RentalController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public RentalController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/register")
    public Mono<String> rentBike(@RequestBody RegisterBike command) {
        return Mono.fromFuture(
                commandGateway.send(command, String.class)
        );
    }

    @PostMapping("/request")
    public Mono<ResponseEntity<String>> requestBike(@RequestBody RequestBike command) {
        return Mono.fromFuture(
                        commandGateway.send(command, String.class)
                ).map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(400).body(e.getMessage())));
    }

    @GetMapping("/bike/{bikeId}")
    public Mono<ResponseEntity<Bike>> getBike(@PathVariable String bikeId) {
        return Mono.fromFuture(
                queryGateway.query(new FindBike(bikeId), Bike.class)
        ).map(ResponseEntity::ok)
         .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/bikes")
    public Mono<List<Bike>> getAvailableBikes(@RequestParam String location) {
        return Mono.fromFuture(
                queryGateway.queryMany(new FindAvailableBikes(location), Bike.class)
        );
    }

    // Exercise 1 — return a rented bike.
    @PostMapping("/return")
    public Mono<ResponseEntity<String>> returnBike(@RequestBody ReturnBike command) {
        return Mono.fromFuture(commandGateway.send(command, Object.class))
                .thenReturn(ResponseEntity.ok("returned"))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(400).body(e.getMessage())));
    }

    // Exercise 2 — member management.
    @PostMapping("/members")
    public Mono<String> registerMember(@RequestBody RegisterMember command) {
        return Mono.fromFuture(
                commandGateway.send(command, String.class)
        );
    }

    @PostMapping("/members/suspend")
    public Mono<ResponseEntity<String>> suspendMember(@RequestBody SuspendMember command) {
        return Mono.fromFuture(commandGateway.send(command, Object.class))
                .thenReturn(ResponseEntity.ok("suspended"))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(400).body(e.getMessage())));
    }

    // Exercise 3 — a member's rental history.
    @GetMapping("/members/{memberId}/rentals")
    public Mono<List<RentalRecord>> getRentalHistory(@PathVariable String memberId) {
        return Mono.fromFuture(
                queryGateway.queryMany(new FindRentalHistory(memberId), RentalRecord.class)
        );
    }
}
