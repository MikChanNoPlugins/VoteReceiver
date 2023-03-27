package dev.mikchan.mcnp.votereceiver.core.log

/**
 * Common logger interface
 */
interface ILogger {
    /**
     * Logs info message
     *
     * @param msg The message
     */
    fun info(msg: String)

    /**
     * Logs warning message
     *
     * @param msg The message
     * @param exception The exception
     */
    fun warning(msg: String?, exception: Throwable? = null)
}
