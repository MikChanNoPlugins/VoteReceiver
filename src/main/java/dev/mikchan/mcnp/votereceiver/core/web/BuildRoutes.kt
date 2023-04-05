package dev.mikchan.mcnp.votereceiver.core.web

import dev.mikchan.mcnp.votereceiver.core.IPlugin
import io.ktor.server.application.*
import io.ktor.server.routing.*

internal fun Application.buildRoutes(plugin: IPlugin) {
    routing {
        createMineServTopRoute(plugin)
        createTMonitoringComRoute(plugin)

        if (plugin.config.testEnabled) {
            createTestRoute(plugin)
        }
    }
}
