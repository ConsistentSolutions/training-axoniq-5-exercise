package io.axoniq.training.trainingaxoniq5.bikerental.api.command;

import org.axonframework.messaging.commandhandling.annotation.Command;

@Command
public record RegisterMember(String name) {
}
