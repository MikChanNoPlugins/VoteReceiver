package dev.mikchan.mcnp.votereceiver.core.log

import java.util.logging.Level
import java.util.logging.Logger

internal class JvmLoggerProxy(private val logger: Logger) : ILogger {
    override fun info(msg: String) {
        logger.info(msg)
    }

    override fun warning(msg: String?, exception: Throwable?) {
        if (exception != null) {
            logger.log(Level.WARNING, msg, exception)
        } else {
            logger.warning(msg)
        }
    }
}
