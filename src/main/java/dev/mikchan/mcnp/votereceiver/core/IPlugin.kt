package dev.mikchan.mcnp.votereceiver.core

import com.vexsoftware.votifier.VoteHandler
import dev.mikchan.mcnp.votereceiver.core.config.IConfig
import dev.mikchan.mcnp.votereceiver.core.log.ILogger
import dev.mikchan.mcnp.votereceiver.core.utility.IUtility
import io.ktor.server.engine.*
import java.util.concurrent.ExecutorService

/**
 * Plugin interface
 */
interface IPlugin {
    /**
     * Manages plugin configuration
     */
    val config: IConfig

    /**
     * Useful functions
     */
    val utility: IUtility

    /**
     * Basically, NuVotifier instance
     */
    val voteHandler: VoteHandler?

    /**
     * A thread pool for concurrent tasks
     */
    val threadPool: ExecutorService

    /**
     * A web server instance
     */
    val webServer: ApplicationEngine

    /**
     * Logger instance
     */
    val log: ILogger
}
