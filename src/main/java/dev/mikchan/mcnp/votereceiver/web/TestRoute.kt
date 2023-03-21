package dev.mikchan.mcnp.votereceiver.web

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.net.VotifierSession
import dev.mikchan.mcnp.votereceiver.VoteReceiverPlugin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal fun Route.createTestRoute(plugin: VoteReceiverPlugin) {
    get("/test") {
        val parameters = try {
            call.receiveParameters()
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Incomplete request")
            return@get
        }

        val username = parameters["username"]

        if (username == null) {
            call.respond(HttpStatusCode.InternalServerError, "The username is not specified")
            return@get
        }

        val vote = Vote(
            "MCNVoteReceiver Test Route",
            username,
            call.request.origin.remoteHost,
            System.currentTimeMillis().toString(10),
        )
        plugin.voteHandler?.onVoteReceived(vote, VotifierSession.ProtocolVersion.UNKNOWN, vote.address)
    }
}
