package io.axoniq.training.trainingaxoniq5.bikerental.api.command;

import org.axonframework.messaging.commandhandling.annotation.Command;
import org.axonframework.modelling.annotation.TargetEntityId;

@Command
public record SuspendMember(@TargetEntityId String memberId) {
}
