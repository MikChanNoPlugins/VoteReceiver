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
 * Reference: `https://mctop.im/awards.zip`
 */
internal fun Route.createMcTopImRoute(plugin: IPlugin) {
    post("/mctop.im") {
        val secretWord = plugin.config.mcTopImSecretWord
        if (secretWord == null) {
            call.respond(HttpStatusCode.InternalServerError, "-1")
            return@post
        }

        val parameters = try {
            call.receiveParameters()
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "-1")
            return@post
        }

        val nick = parameters["nickname"]
        val token = parameters["token"]

        if (nick == null || token == null) {
            call.respond(HttpStatusCode.InternalServerError, "-1")
            return@post
        }

        val hash = plugin.utility.md5(nick + secretWord)
        if (token != hash) {
            call.respond(HttpStatusCode.InternalServerError, "-2")
            return@post
        }

        val vote = Vote("mctop.im", nick, call.request.origin.remoteHost, System.currentTimeMillis().toString(10))
        plugin.voteHandler?.onVoteReceived(vote, VotifierSession.ProtocolVersion.UNKNOWN, vote.address)

        call.respond("1")
    }
}
