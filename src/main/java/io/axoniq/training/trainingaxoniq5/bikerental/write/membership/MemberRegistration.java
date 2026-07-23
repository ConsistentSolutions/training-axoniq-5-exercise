package io.axoniq.training.trainingaxoniq5.bikerental.write.membership;

import io.axoniq.training.trainingaxoniq5.bikerental.api.command.RegisterMember;
import io.axoniq.training.trainingaxoniq5.bikerental.api.command.SuspendMember;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.MemberRegistered;
import io.axoniq.training.trainingaxoniq5.bikerental.api.event.MemberSuspended;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;
import org.axonframework.modelling.annotation.InjectEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Provided as-is for Exercise 2. Registers members and suspends them. Mirrors {@code BikeRegistration}
 * and {@code TripCommandHandler}, so you can create the members that {@code TripCommandHandler} checks.
 */
@Component
public class MemberRegistration {

    @CommandHandler
    public String handle(RegisterMember command, EventAppender eventAppender) {
        String memberId = UUID.randomUUID().toString();
        eventAppender.append(new MemberRegistered(memberId, command.name()));
        return memberId;
    }

    @CommandHandler
    public void handle(SuspendMember command, EventAppender eventAppender, @InjectEntity MemberState memberState) {
        eventAppender.append(new MemberSuspended(command.memberId()));
    }
}
