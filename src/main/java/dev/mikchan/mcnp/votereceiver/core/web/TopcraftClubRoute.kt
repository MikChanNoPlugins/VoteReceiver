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
 * Reference `https://topcraft.club/static/download/topcraft_gift.php`
 */
internal fun Route.createTopcraftClubRoute(plugin: IPlugin) {
    post("/topcraft.club") {
        val secret = plugin.config.topcraftClubKey
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
        val timestamp = parameters["timestamp"]
        val signature = parameters["signature"]

        if (username == null || timestamp == null || signature == null) {
            call.respond(HttpStatusCode.InternalServerError, "Bad login")
            return@post
        }

        val template = "$username$timestamp$secret"
        val hash = plugin.utility.sha256(template)
        if (signature != hash) {
            call.respond(HttpStatusCode.InternalServerError, "Signature check failed")
            return@post
        }

        val vote = Vote("topcraft.club", username, call.request.origin.remoteHost, timestamp)
        plugin.voteHandler?.onVoteReceived(vote, VotifierSession.ProtocolVersion.UNKNOWN, vote.address)

        call.respond("done")
    }
}
