package dev.mikchan.mcnp.votereceiver.factory.spigot

import com.vexsoftware.votifier.NuVotifierBukkit
import dev.mikchan.mcnp.votereceiver.VoteReceiverSpigotPlugin
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
import java.io.File

internal class SpigotFactory(private val plugin: VoteReceiverSpigotPlugin) : IFactory {
    override fun createConfig(): IConfig {
        val resource = plugin.getResource("config.yml")

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

    override fun createVoteHandler(): NuVotifierBukkit? {
        return plugin.server.pluginManager.getPlugin("Votifier") as? NuVotifierBukkit
    }
}
