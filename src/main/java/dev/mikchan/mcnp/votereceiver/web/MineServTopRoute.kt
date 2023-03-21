package dev.mikchan.mcnp.votereceiver.web

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.net.VotifierSession
import dev.mikchan.mcnp.votereceiver.VoteReceiverSpigotPlugin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Reference `https://github.com/kartashovio/reward-system-docs/releases`
 */
internal fun Route.createMineServTopRoute(plugin: VoteReceiverSpigotPlugin) {
    post("/mineserv.top") {
        val secret = plugin.config.mineServTopKey
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

        val project = parameters["project"]
        val username = parameters["username"]
        val timestamp = parameters["timestamp"]
        val signature = parameters["signature"]

        if (project == null || username == null || timestamp == null || signature == null) {
            call.respond(HttpStatusCode.InternalServerError, "Incomplete request")
            return@post
        }

        val template = "$project.$secret.$timestamp.$username"
        val hash = plugin.utility.sha256(template)
        if (signature != hash) {
            call.respond(HttpStatusCode.InternalServerError, "Signature check failed")
            return@post
        }

        val vote = Vote("mineserv.top/$project", username, call.request.origin.remoteHost, timestamp)
        plugin.voteHandler?.onVoteReceived(vote, VotifierSession.ProtocolVersion.UNKNOWN, vote.address)

        call.respond("done")
    }
}
