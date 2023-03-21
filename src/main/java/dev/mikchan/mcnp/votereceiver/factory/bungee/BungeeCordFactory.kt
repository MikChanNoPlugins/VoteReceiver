package dev.mikchan.mcnp.votereceiver.factory.bungee

import com.vexsoftware.votifier.bungee.NuVotifier
import dev.mikchan.mcnp.votereceiver.VoteReceiverBungeeCordPlugin
import dev.mikchan.mcnp.votereceiver.config.IConfig
import dev.mikchan.mcnp.votereceiver.config.boosted.BoostedYamlConfig
import dev.mikchan.mcnp.votereceiver.config.fallback.FallbackConfig
import dev.mikchan.mcnp.votereceiver.factory.IFactory
import dev.mikchan.mcnp.votereceiver.utility.IUtility
import dev.mikchan.mcnp.votereceiver.utility.base.Utility
import dev.mikchan.mcnp.votereceiver.web.createMineServTopRoute
import dev.mikchan.mcnp.votereceiver.web.createTMonitoringComRoute
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import net.md_5.bungee.api.ProxyServer
import java.io.File

internal class BungeeCordFactory(private val plugin: VoteReceiverBungeeCordPlugin) : IFactory {
    override fun createConfig(): IConfig {
        val resource = plugin.getResourceAsStream("config.yml")

        return if (resource != null) {
            BoostedYamlConfig(File(plugin.dataFolder, "config.yml"), resource)
        } else {
            FallbackConfig()
        }
    }

    override fun createUtility(): IUtility {
        return Utility()
    }

    override fun createApplicationEngine(): ApplicationEngine {
        return embeddedServer(Netty, port = plugin.config.port) {
            routing {
                createMineServTopRoute(plugin)
                createTMonitoringComRoute(plugin)
            }
        }
    }

    override fun createVoteHandler(): NuVotifier? {
        return ProxyServer.getInstance().pluginManager.getPlugin("NuVotifier") as? NuVotifier
    }
}
