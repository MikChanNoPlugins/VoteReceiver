package dev.mikchan.mcnp.votereceiver.core.web

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.net.VotifierSession
import dev.mikchan.mcnp.votereceiver.core.IPlugin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Reference: `https://cdn.minecraftrating.ru/uploads/promotion.zip`
 */
internal fun Route.createMinecraftratingRuRoute(plugin: IPlugin) {
    post("/minecraftrating.ru") {
        val secret = plugin.config.minecraftingRuKey
        if (secret == null) {
            call.respond(HttpStatusCode.InternalServerError, "Key is not specified")
            return@post
        }

        val parameters = try {
            call.receiveParameters()
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Incomplete request")
            return@post
        }

        val username = parameters["username"]
        val ip = parameters["ip"]
        val timestamp = parameters["timestamp"]
        val signature = parameters["signature"]

        if (ip == null || username == null || timestamp == null || signature == null) {
            call.respond(HttpStatusCode.InternalServerError, "Incomplete request")
            return@post
        }

        val template = "$username$timestamp$secret"
        val hash = plugin.utility.sha1(template)
        if (signature != hash) {
            call.respond(HttpStatusCode.InternalServerError, "Signature check failed")
            return@post
        }

        val vote = Vote(
            "minecraftrating.ru/$ip",
            username,
            call.request.origin.remoteHost,
            System.currentTimeMillis().toString(10)
        )
        plugin.voteHandler?.onVoteReceived(vote, VotifierSession.ProtocolVersion.UNKNOWN, vote.address)

        call.respond("ok")
    }
}
