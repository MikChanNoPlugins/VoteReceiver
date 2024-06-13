package dev.mikchan.mcnp.votereceiver.core.web

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.net.VotifierSession
import dev.mikchan.mcnp.votereceiver.core.IPlugin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal fun Route.createTestRoute(plugin: IPlugin) {
    plugin.log.warning("!!! TEST ROUTE IS ENABLED. IF THIS IS NOT INTENDED, SHOUT DOWN THE SERVER IMMEDIATELY AND DISABLE IT IN config.yml !!!")

    get("/test") {
        val username = call.request.queryParameters["username"]

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
        plugin.voteHandler?.onVoteReceived(vote, VotifierSession.ProtocolVersion.TEST, vote.address)

        call.respond("Ok")
    }
}
