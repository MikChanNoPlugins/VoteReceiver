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
}
