package dev.mikchan.mcnp.votereceiver.factory

import com.vexsoftware.votifier.NuVotifierBukkit
import com.vexsoftware.votifier.VoteHandler
import dev.mikchan.mcnp.votereceiver.config.IConfig
import dev.mikchan.mcnp.votereceiver.utility.IUtility
import io.ktor.server.engine.*

/**
 * A plugin factory
 *
 * Generates essential objects for plugin to function
 */
interface IFactory {
    /**
     * Creates a configuration object
     *
     * @return A configuration object
     */
    fun createConfig(): IConfig

    /**
     * Creates a utility object
     *
     * @return A utility object
     */
    fun createUtility(): IUtility

    /**
     * Creates a web engine
     *
     * @return A web engine
     */
    fun createApplicationEngine(): ApplicationEngine

    /**
     * Creates a vote handler
     *
     * @return A [NuVotifierBukkit] instance
     */
    fun createVoteHandler(): VoteHandler?
}
