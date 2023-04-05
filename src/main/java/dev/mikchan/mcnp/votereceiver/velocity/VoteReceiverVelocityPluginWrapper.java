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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * Main velocity plugin class
 */
@Plugin(id = "mikchan-no-vote-receiver",
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

    /**
     * The constructor
     *
     * @param server        The server instance
     * @param logger        The logger instance
     * @param dataDirectory The plugin data directory
     */
    @Inject
    public VoteReceiverVelocityPluginWrapper(ProxyServer server, Logger logger,
                                             @DataDirectory
                                             Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    private static void withJekaPath(File path, Runnable runnable) {
        final Properties props = System.getProperties();
        final String jekaCacheDirProp = "jeka.cache.dir";
        final Object oldJekaCacheDir = props.get(jekaCacheDirProp);
        props.put(jekaCacheDirProp, path.toString());

        runnable.run();

        if (oldJekaCacheDir != null) {
            props.put(jekaCacheDirProp, oldJekaCacheDir);
        } else {
            props.remove(jekaCacheDirProp);
        }
    }

    /**
     * The server instance
     *
     * @return The server instance
     */
    public ProxyServer getServer() {
        return server;
    }

    /**
     * The logger instance
     *
     * @return The logger instance
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * The plugin data directory
     *
     * @return The plugin data directory
     */
    public Path getDataDirectory() {
        return dataDirectory;
    }


    /**
     * ProxyInitializeEvent listener
     *
     * @param event The event instance
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            registerDependencies();

            plugin = new VoteReceiverVelocityPlugin(this);
            plugin.getWebServer().start(true);
        } catch (IOException e) {
            logger.error("Unable to enable plugin");
        }
    }

    /**
     * ProxyShutdownEvent listener
     *
     * @param event The event instance
     */
    @Subscribe
    public void onProxyShutdownEvent(ProxyShutdownEvent event) {
        if (plugin == null) return;
        plugin.getWebServer().stop(500, 500);
        plugin.getThreadPool().shutdown();
    }

    private void download(URL url, File location) throws IOException {
        if (logger != null) logger.info("Downloading " + url + "...");

        //noinspection ResultOfMethodCallIgnored
        location.getParentFile().mkdirs();

        try (BufferedInputStream in = new BufferedInputStream(url.openStream());
             FileOutputStream output = new FileOutputStream(location)) {
            byte[] buffer = new byte[1024];
            int bytes;
            while ((bytes = in.read(buffer, 0, 1024)) != -1) {
                output.write(buffer, 0, bytes);
            }
        } catch (IOException e) {
            if (logger != null)
                logger.error("Failed to download " + url + "...");
            throw e;
        }
    }

    private void registerDependencies() throws IOException {
        final File libsPath = new File(getDataDirectory().toFile(), "libs");
        //noinspection ResultOfMethodCallIgnored
        libsPath.mkdirs();

        final File jekaFile = new File(libsPath, "jeka-core-0.10.12.jar");
        if (!jekaFile.exists()) {
            final URL jekaUrl = new URL("https", "repo1.maven.org",
                    "maven2/dev/jeka/jeka-core/0.10.12/jeka-core-0.10.12.jar");
            download(jekaUrl, jekaFile);
        }

        getServer().getPluginManager().addToClasspath(this, jekaFile.toPath());

        withJekaPath(new File(libsPath, "cache"), () -> {
            final JkDependencySet deps = JkDependencySet.of()
                    .and("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
                    .and("io.ktor:ktor-server-core:2.2.4")
                    .and("io.ktor:ktor-server-core-jvm:2.2.4")
                    .and("io.ktor:ktor-server-netty:2.2.4")
                    .and("io.ktor:ktor-server-netty-jvm:2.2.4")
                    .and("dev.dejvokep:boosted-yaml:1.3")
                    .and("com.xk72:pherialize:1.2.4");
            final JkDependencyResolver resolver = JkDependencyResolver.of()
                    .setRepos(JkRepo.ofMavenCentral().toSet());
            final List<Path> paths =
                    resolver.resolve(deps).getFiles().getEntries();

            for (final Path path : paths) {
                getServer().getPluginManager().addToClasspath(this, path);
            }
        });
    }
}
