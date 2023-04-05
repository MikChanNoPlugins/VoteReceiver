package dev.mikchan.mcnp.votereceiver.spigot.factory

import com.vexsoftware.votifier.NuVotifierBukkit
import dev.mikchan.mcnp.votereceiver.core.config.IConfig
import dev.mikchan.mcnp.votereceiver.core.factory.CommonFactory
import dev.mikchan.mcnp.votereceiver.spigot.VoteReceiverSpigotPlugin
import java.io.File

internal class SpigotFactory(private val plugin: VoteReceiverSpigotPlugin) : CommonFactory(plugin) {
    override fun createConfig(): IConfig {
        val resource = plugin.getResource("config.yml")
        val document = File(plugin.dataFolder, "config.yml")
        return createConfig(document, resource)
    }

    override fun createVoteHandler(): NuVotifierBukkit? {
        return plugin.server.pluginManager.getPlugin("Votifier") as? NuVotifierBukkit
    }
}
