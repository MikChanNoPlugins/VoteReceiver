package dev.mikchan.mcnp.votereceiver.web

import com.vexsoftware.votifier.model.Vote
import com.vexsoftware.votifier.net.VotifierSession
import de.ailis.pherialize.Pherialize
import dev.mikchan.mcnp.votereceiver.VoteReceiverPlugin
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.logging.Level

/**
 * Reference `https://tmonitoring.com/uploads/files/top.zip`
 */
internal fun Route.createTMonitoringComRoute(plugin: VoteReceiverPlugin) {
    get("/tmonitoring.com") {
        val hash = call.request.queryParameters["hash"]
        plugin.logger.info("TMonitoring: $hash")
        val id = call.request.queryParameters["id"]
        plugin.logger.info("TMonitoring: $id")

        if (hash == null || id == null) {
            call.respond("An error has occurred")
            return@get
        }

        val address = call.request.origin.remoteHost

        plugin.threadPool.submit {
            try {
                val client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build()
                val request =
                    HttpRequest.newBuilder().uri(URI.create("https://tmonitoring.com/api/check/$hash/?id=$id")).build()

                val response = client.send(request, HttpResponse.BodyHandlers.ofString())
                val result = response.body() ?: return@submit
                plugin.logger.info("TMonitoring: $result")

                val data = Pherialize.unserialize(result).toArray() ?: return@submit
                plugin.logger.info("TMonitoring: $data")

                val remoteHash = data.getString("hash")
                plugin.logger.info("TMonitoring: $remoteHash")

                if (hash != remoteHash) return@submit

                val username = data.getString("username") ?: return@submit
                plugin.logger.info("TMonitoring: $username")

                val vote = Vote("tmonitoring.com", username, address, System.currentTimeMillis().toString(10))
                plugin.voteHandler?.onVoteReceived(vote, VotifierSession.ProtocolVersion.UNKNOWN, address)

            } catch (ex: Exception) {
                plugin.logger.log(Level.WARNING, ex.message, ex)
            }
        }
    }
}
