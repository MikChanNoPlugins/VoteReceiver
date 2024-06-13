package dev.mikchan.mcnp.votereceiver.velocity

import com.vexsoftware.votifier.VoteHandler
import dev.mikchan.mcnp.votereceiver.core.IPlugin
import dev.mikchan.mcnp.votereceiver.core.config.IConfig
import dev.mikchan.mcnp.votereceiver.core.factory.IFactory
import dev.mikchan.mcnp.votereceiver.core.log.ILogger
import dev.mikchan.mcnp.votereceiver.core.log.Sl4jLoggerProxy
import dev.mikchan.mcnp.votereceiver.core.utility.IUtility
import dev.mikchan.mcnp.votereceiver.velocity.factory.VelocityFactory
import io.ktor.server.engine.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class VoteReceiverVelocityPlugin(val velocity: VoteReceiverVelocityPluginWrapper) : IPlugin {
    private val factory: IFactory by lazy { VelocityFactory(this) }

    override val config: IConfig by lazy { factory.createConfig() }
    override val utility: IUtility by lazy { factory.createUtility() }
    override val voteHandler: VoteHandler? by lazy { factory.createVoteHandler() }
    override val threadPool: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    override val webServer: ApplicationEngine by lazy { factory.createApplicationEngine() }
    override val log: ILogger by lazy { Sl4jLoggerProxy(velocity.logger) }
}
