package dev.mikchan.mcnp.votereceiver.config

/**
 * Describes configuration
 */
interface IConfig {
    /**
     * Reloads config
     *
     * @return `true` if reloaded successfully, `false` otherwise
     */
    fun reload(): Boolean

    /**
     * Port which the plugin will listen to
     */
    val port: Int

    /**
     * A secret key of `mineserv.top`
     */
    val mineServTopKey: String?

    /**
     * Determines, if the test route accessible.
     *
     * WARNING: Should NEVER be `true` in production
     */
    val testEnabled: Boolean
}
