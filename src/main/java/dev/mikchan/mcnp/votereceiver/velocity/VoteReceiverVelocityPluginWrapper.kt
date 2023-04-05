package dev.mikchan.mcnp.votereceiver.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.jeka.core.api.depmanagement.JkDependencySet
import dev.jeka.core.api.depmanagement.JkRepo
import dev.jeka.core.api.depmanagement.resolution.JkDependencyResolver
import org.slf4j.Logger
import java.io.File
import java.io.IOException
import java.net.URL
import java.nio.file.Path

// DO NOT USE KOTLIN STDLIB IN THIS CLASS!!!
/**
 * Main velocity plugin class
 */
class VoteReceiverVelocityPluginWrapper
@Inject constructor(
    /**
     * The server instance
     *
     * @return The server instance
     */
    val server: ProxyServer,
    /**
     * The logger instance
     *
     * @return The logger instance
     */
    val logger: Logger,
    /**
     * The plugin data directory
     *
     * @return The plugin data directory
     */
    @param:DataDirectory val dataDirectory: Path
) {
    private var plugin: VoteReceiverVelocityPlugin? = null

    /**
     * ProxyInitializeEvent listener
     *
     * @param event The event instance
     */
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent?) {
        try {
            registerDependencies()
            plugin = VoteReceiverVelocityPlugin(this)
            plugin!!.webServer.start(true)
        } catch (e: IOException) {
            logger.error("Unable to enable plugin")
        }
    }

    /**
     * ProxyShutdownEvent listener
     *
     * @param event The event instance
     */
    @Subscribe
    fun onProxyShutdownEvent(event: ProxyShutdownEvent?) {
        if (plugin == null) return
        plugin!!.webServer.stop(500, 500)
        plugin!!.threadPool.shutdown()
    }

    @Throws(IOException::class)
    private fun download(url: URL, location: File) {
        logger.info("Downloading $url...")
        location.parentFile.mkdirs()
        try {
            Downloader.download(url, location)
        } catch (e: IOException) {
            logger.error("Failed to download $url...")
            throw e
        }
    }

    @Throws(IOException::class)
    private fun registerDependencies() {
        logger.info("Start loading dependencies...")

        val libsPath = File(dataDirectory.toFile(), "libs")
        libsPath.mkdirs()
        val jekaFile = File(libsPath, "jeka-core-0.10.12.jar")
        if (!jekaFile.exists()) {
            val jekaUrl = URL(
                "https", "repo1.maven.org",
                "maven2/dev/jeka/jeka-core/0.10.12/jeka-core-0.10.12.jar"
            )
            download(jekaUrl, jekaFile)
        }
        server.pluginManager.addToClasspath(this, jekaFile.toPath())
        withJekaPath(File(libsPath, "cache")) {
            val deps = JkDependencySet.of()
                .and("org.jetbrains.kotlin:kotlin-stdlib:1.8.20")
                .and("io.ktor:ktor-server-core:2.2.4")
                .and("io.ktor:ktor-server-core-jvm:2.2.4")
                .and("io.ktor:ktor-server-netty:2.2.4")
                .and("io.ktor:ktor-server-netty-jvm:2.2.4")
                .and("dev.dejvokep:boosted-yaml:1.3")
                .and("com.xk72:pherialize:1.2.4")
            val resolver = JkDependencyResolver.of()
                .setRepos(JkRepo.ofMavenCentral().toSet())
            val paths = resolver.resolve(deps).files.entries
            for (path in paths) {
                server.pluginManager.addToClasspath(this, path)
            }
        }

        logger.info("Done loading dependencies!")
    }

    companion object {
        private fun withJekaPath(path: File, runnable: Runnable) {
            val props = System.getProperties()
            val jekaCacheDirProp = "jeka.cache.dir"
            val oldJekaCacheDir = props[jekaCacheDirProp]
            props[jekaCacheDirProp] = path.toString()
            runnable.run()
            if (oldJekaCacheDir != null) {
                props[jekaCacheDirProp] = oldJekaCacheDir
            } else {
                props.remove(jekaCacheDirProp)
            }
        }
    }
}
