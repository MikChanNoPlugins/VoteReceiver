package dev.mikchan.mcnp.votereceiver.spigot

import com.vexsoftware.votifier.VoteHandler
import dev.mikchan.mcnp.votereceiver.core.IPlugin
import dev.mikchan.mcnp.votereceiver.core.config.IConfig
import dev.mikchan.mcnp.votereceiver.core.factory.IFactory
import dev.mikchan.mcnp.votereceiver.core.log.ILogger
import dev.mikchan.mcnp.votereceiver.core.log.JvmLoggerProxy
import dev.mikchan.mcnp.votereceiver.core.utility.IUtility
import dev.mikchan.mcnp.votereceiver.spigot.factory.SpigotFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Main spigot plugin class
 */
@Suppress("unused")
class VoteReceiverSpigotPlugin : JavaPlugin(), IPlugin {
    companion object {
        private const val bStatsId = 17922
    }

    private val factory: IFactory by lazy { SpigotFactory(this) }

    override val config: IConfig by lazy { factory.createConfig() }
    override val utility: IUtility by lazy { factory.createUtility() }
    override val voteHandler: VoteHandler? by lazy { factory.createVoteHandler() }
    override val threadPool: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    override val webServer: ApplicationEngine by lazy { factory.createApplicationEngine() }
    override val log: ILogger by lazy { JvmLoggerProxy(this.logger) }

    override fun onEnable() {
        webServer.start()

        Metrics(this, bStatsId)
    }

    override fun onDisable() {
        webServer.stop()
        threadPool.shutdownNow()
    }
}
