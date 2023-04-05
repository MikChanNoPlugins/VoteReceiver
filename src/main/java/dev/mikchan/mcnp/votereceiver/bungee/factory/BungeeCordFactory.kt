package dev.mikchan.mcnp.votereceiver.bungee.factory

import com.vexsoftware.votifier.bungee.NuVotifier
import dev.mikchan.mcnp.votereceiver.bungee.VoteReceiverBungeeCordPlugin
import dev.mikchan.mcnp.votereceiver.core.config.IConfig
import dev.mikchan.mcnp.votereceiver.core.factory.CommonFactory
import net.md_5.bungee.api.ProxyServer
import java.io.File

internal class BungeeCordFactory(private val plugin: VoteReceiverBungeeCordPlugin) : CommonFactory(plugin) {
    override fun createConfig(): IConfig {
        val resource = plugin.getResourceAsStream("config.yml")
        val document = File(plugin.dataFolder, "config.yml")
        return createConfig(document, resource)
    }

    override fun createVoteHandler(): NuVotifier? {
        return ProxyServer.getInstance().pluginManager.getPlugin("NuVotifier") as? NuVotifier
    }
}
