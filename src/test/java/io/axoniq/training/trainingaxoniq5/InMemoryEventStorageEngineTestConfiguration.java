package io.axoniq.training.trainingaxoniq5;

import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Provides an {@link InMemoryEventStorageEngine} for tests, replacing the JPA-based engine that is
 * auto-configured for the application. Declared {@link AutoConfigureBefore before} Axon's
 * {@code AxonTestConfiguration} (package-private, hence referenced by name) so the in-memory engine
 * is the one wired into the {@code AxonTestFixture}.
 *
 * This is required in order to have an EventStorageEngine that supports DCB, without connecting to an
 * Axon Server TestContainer.
 *
 * Registered as a test auto-configuration (see
 * {@code src/test/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}),
 * so it applies to every {@code @AxonSpringBootTest} (indeed every Spring Boot test) context without
 * an explicit {@code @Import}. It only activates when Axon Server is disabled, leaving tests that run
 * against a real Axon Server (e.g. via an {@code AxonServerContainer}) untouched.
 */
@AutoConfiguration
@AutoConfigureBefore(name = "org.axonframework.extension.springboot.test.AxonTestConfiguration")
@ConditionalOnProperty(name = "axon.axonserver.enabled", havingValue = "false")
public class InMemoryEventStorageEngineTestConfiguration {

    @Bean
    public InMemoryEventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }
}
