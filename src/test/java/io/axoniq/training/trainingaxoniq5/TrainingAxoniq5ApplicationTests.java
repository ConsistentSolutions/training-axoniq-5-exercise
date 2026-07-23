package io.axoniq.training.trainingaxoniq5;

import io.axoniq.framework.testcontainer.AxonServerContainer;
import org.axonframework.extension.springboot.test.AxonSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@AxonSpringBootTest
@Testcontainers
class TrainingAxoniq5ApplicationTests {

    @Container
    @ServiceConnection
    static AxonServerContainer axonServer = new AxonServerContainer().withDcbContext(true);

    @Test
    void contextLoads() {
    }
}
