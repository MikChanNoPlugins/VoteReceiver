package dev.mikchan.mcnp.votereceiver.web

import dev.mikchan.mcnp.votereceiver.VoteReceiverPlugin
import io.ktor.server.routing.*

internal fun Route.createTMonitoringComRoute(plugin: VoteReceiverPlugin) {
    post("/tmonitoring.com") {}
}
