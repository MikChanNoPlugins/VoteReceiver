package dev.mikchan.mcnp.votereceiver.core.log

import org.slf4j.Logger

internal class Sl4jLoggerProxy(private val logger: Logger) : ILogger {
    override fun info(msg: String) {
        logger.info(msg)
    }

    override fun warning(msg: String?, exception: Throwable?) {
        if (exception != null) {
            logger.warn(msg, exception)
        } else {
            logger.warn(msg)
        }
    }
}
