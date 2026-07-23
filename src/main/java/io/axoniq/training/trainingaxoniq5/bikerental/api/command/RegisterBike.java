package io.axoniq.training.trainingaxoniq5.bikerental.api.command;

import org.axonframework.messaging.commandhandling.annotation.Command;

@Command
public record RegisterBike(String bikeType,
                           String location) {
}
