package dev.mikchan.mcnp.votereceiver

import com.vexsoftware.votifier.NuVotifierBukkit
import dev.mikchan.mcnp.votereceiver.config.IConfig
import dev.mikchan.mcnp.votereceiver.factory.IFactory
import dev.mikchan.mcnp.votereceiver.factory.spigot.SpigotFactory
import dev.mikchan.mcnp.votereceiver.utility.IUtility
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
 * Main plugin class
 */
@Suppress("unused")
class VoteReceiverSpigotPlugin : JavaPlugin() {
    companion object {
        private const val bStatsId = 17922
    }

    private val factory: IFactory by lazy { SpigotFactory(this) }

    val config: IConfig by lazy { factory.createConfig() }
    val utility: IUtility by lazy { factory.createUtility() }
    val voteHandler: NuVotifierBukkit? by lazy { factory.createVoteHandler() }
    val threadPool: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private val webServer: ApplicationEngine by lazy { factory.createApplicationEngine() }

    override fun onEnable() {
        webServer.start()

        Metrics(this, bStatsId)
    }

    override fun onDisable() {
        webServer.stop()
        threadPool.shutdownNow()
    }
}
