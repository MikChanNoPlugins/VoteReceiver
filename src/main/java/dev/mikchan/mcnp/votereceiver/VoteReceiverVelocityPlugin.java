package dev.mikchan.mcnp.votereceiver;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(id = "mcn_vote-receiver", name = "MikChanのVoteReceiver", version = "0.1.0-SNAPSHOT",
        url = "https://github.com/MikChanNoPlugins/VoteReceiver",
        description = "Converts various monitoring vote systems to Votifier",
        authors = {"George Endo (wtlgo / MikChan)"})
public class VoteReceiverVelocityPlugin {
    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VoteReceiverVelocityPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        getLogger().info("Ping pong");
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }
}
