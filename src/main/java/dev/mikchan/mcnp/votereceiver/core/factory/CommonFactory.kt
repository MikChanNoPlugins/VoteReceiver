package dev.mikchan.mcnp.votereceiver.core.factory

import dev.mikchan.mcnp.votereceiver.core.IPlugin
import dev.mikchan.mcnp.votereceiver.core.config.IConfig
import dev.mikchan.mcnp.votereceiver.core.config.boosted.BoostedYamlConfig
import dev.mikchan.mcnp.votereceiver.core.config.fallback.FallbackConfig
import dev.mikchan.mcnp.votereceiver.core.utility.IUtility
import dev.mikchan.mcnp.votereceiver.core.utility.base.Utility
import dev.mikchan.mcnp.votereceiver.core.web.buildRoutes
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File
import java.io.InputStream

internal abstract class CommonFactory(private val plugin: IPlugin) : IFactory {
    protected fun createConfig(document: File?, resource: InputStream?): IConfig {
        return if (document == null || resource == null) {
            FallbackConfig()
        } else {
            BoostedYamlConfig(document, resource)
        }
    }

    override fun createUtility(): IUtility {
        return Utility()
    }

    override fun createApplicationEngine(): ApplicationEngine {
        return embeddedServer(Netty, port = plugin.config.port) {
            buildRoutes(plugin)
        }
    }
}
