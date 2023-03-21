package dev.mikchan.mcnp.votereceiver

import com.vexsoftware.votifier.NuVotifierBukkit
import dev.mikchan.mcnp.votereceiver.config.IConfig
import dev.mikchan.mcnp.votereceiver.factory.IFactory
import dev.mikchan.mcnp.votereceiver.utility.IUtility
import io.ktor.server.engine.*
import net.md_5.bungee.api.plugin.Plugin
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Main bungeeecord plugin class
 */
@Suppress("unused")
class VoteReceiverBungeeCordPlugin : Plugin(), IPlugin {
    private val factory: IFactory by lazy { TODO("Not implemented yet") }

    override val config: IConfig by lazy { factory.createConfig() }
    override val utility: IUtility by lazy { factory.createUtility() }
    override val voteHandler: NuVotifierBukkit? by lazy { factory.createVoteHandler() }
    override val threadPool: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    override val webServer: ApplicationEngine by lazy { factory.createApplicationEngine() }

    override fun onEnable() {
        webServer.start()
    }

    override fun onDisable() {
        webServer.stop()
        threadPool.shutdownNow()
    }
}
