package io.Odyssey.test;

import io.Odyssey.Server;
import io.Odyssey.ServerConfiguration;
import io.Odyssey.ServerState;
import io.Odyssey.model.entity.player.Player;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public class ServerTest {

    private final ServerConfiguration configuration;

    public ServerTest(ServerConfiguration configuration) {
        this.configuration = configuration;
        try {
            Server.startServerless(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServerTest(ServerState serverState) {
        this(TestServerConfiguration.getConfiguration(serverState));
    }

    public ServerConfiguration getConfiguration() {
        return configuration;
    }
}
