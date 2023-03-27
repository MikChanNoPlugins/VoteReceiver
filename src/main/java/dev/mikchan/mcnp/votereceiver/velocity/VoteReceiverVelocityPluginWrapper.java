package dev.mikchan.mcnp.votereceiver.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkRepo;
import dev.jeka.core.api.depmanagement.resolution.JkDependencyResolver;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

@Plugin(id = "mcn_vote-receiver",
        name = "MikChanNoVoteReceiver",
        version = "0.1.0-SNAPSHOT",
        url = "https://github.com/MikChanNoPlugins/VoteReceiver",
        description = "Converts various monitoring vote systems to Votifier",
        authors = {"George Endo (wtlgo / MikChan)"},
        dependencies = {@Dependency(id = "nuvotifier")})
public class VoteReceiverVelocityPluginWrapper {
    private final ProxyServer server;
    private final Logger logger;

    private final Path dataDirectory;
    private VoteReceiverVelocityPlugin plugin;

    @Inject
    public VoteReceiverVelocityPluginWrapper(ProxyServer server, Logger logger,
                                             @DataDirectory
                                             Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        registerDependencies();
        plugin = new VoteReceiverVelocityPlugin(this);

        plugin.getWebServer().start(true);
    }

    @Subscribe
    void onProxyShutdownEvent(ProxyShutdownEvent event) {
        plugin.getWebServer().stop(500, 500);
        plugin.getThreadPool().shutdown();
    }

    private void download(URL url, File location) {
        try {
            if (logger != null) logger.info("Downloading " + url + "...");

            //noinspection ResultOfMethodCallIgnored
            location.getParentFile().mkdirs();

            try (BufferedInputStream in = new BufferedInputStream(
                    url.openStream());
                 FileOutputStream output = new FileOutputStream(location)) {
                byte[] buffer = new byte[1024];
                int bytes;
                while ((bytes = in.read(buffer, 0, 1024)) != -1) {
                    output.write(buffer, 0, bytes);
                }
            }
        } catch (Exception exception) {
            if (logger != null)
                logger.error("Failed to download " + url + "...");
            throw new RuntimeException(exception);
        }
    }


    private void registerDependencies() {
        try {
            final URL jekaUrl =
                    new URL("https://repo1.maven.org/maven2/dev/jeka/jeka-core/0.10.11/jeka-core-0.10.11.jar");
            final File jekaPath = new File(getDataDirectory().toFile(), "libs");
            //noinspection ResultOfMethodCallIgnored
            jekaPath.mkdirs();

            final File jekaFile = new File(jekaPath, "jeka-core-0.10.11.jar");
            if (!jekaFile.exists()) {
                download(jekaUrl, jekaFile);
            }

            getServer().getPluginManager()
                    .addToClasspath(this, jekaFile.toPath());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        final JkDependencySet deps = JkDependencySet.of()
                .and("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
                .and("io.ktor:ktor-server-core:2.2.4")
                .and("io.ktor:ktor-server-core-jvm:2.2.4")
                .and("io.ktor:ktor-server-netty:2.2.4")
                .and("io.ktor:ktor-server-netty-jvm:2.2.4")
                .and("dev.dejvokep:boosted-yaml:1.3")
                .and("com.xk72:pherialize:1.2.4");
        final JkDependencyResolver resolver = JkDependencyResolver.of()
                .setRepos(JkRepo.ofMavenCentral().toSet());
        final List<Path> paths = resolver.resolve(deps).getFiles().getEntries();

        for (final Path path : paths) {
            getServer().getPluginManager().addToClasspath(this, path);
        }
    }
}
