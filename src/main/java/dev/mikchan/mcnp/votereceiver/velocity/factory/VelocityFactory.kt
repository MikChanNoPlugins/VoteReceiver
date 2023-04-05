package dev.mikchan.mcnp.votereceiver.velocity.factory

import com.vexsoftware.votifier.VoteHandler
import dev.mikchan.mcnp.votereceiver.core.config.IConfig
import dev.mikchan.mcnp.votereceiver.core.factory.CommonFactory
import dev.mikchan.mcnp.votereceiver.velocity.VoteReceiverVelocityPlugin
import java.io.File
import kotlin.jvm.optionals.getOrNull

internal class VelocityFactory(private val plugin: VoteReceiverVelocityPlugin) : CommonFactory(plugin) {
    override fun createConfig(): IConfig {
        val resource = this.javaClass.classLoader.getResourceAsStream("config.yml")
        val document = File(plugin.velocity.dataDirectory.toFile(), "config.yml")
        return createConfig(document, resource)
    }

    override fun createVoteHandler(): VoteHandler? {
        return plugin.velocity.server.pluginManager.getPlugin("nuvotifier").getOrNull()?.instance?.get() as? VoteHandler
    }
}
