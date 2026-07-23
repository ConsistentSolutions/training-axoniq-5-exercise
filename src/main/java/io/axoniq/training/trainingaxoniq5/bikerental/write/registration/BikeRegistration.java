package io.axoniq.training.trainingaxoniq5.bikerental.write.registration;

import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RegisterBike;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.BikeRegistered;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BikeRegistration {

    @CommandHandler
    public String handle(RegisterBike command, EventAppender eventAppender) {
        String bikeId = UUID.randomUUID().toString();
        eventAppender.append(new BikeRegistered(bikeId, command.bikeType(), command.location()));
        return bikeId;
    }

}
