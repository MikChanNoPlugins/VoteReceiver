package dev.mikchan.mcnp.votereceiver.config.fallback

import dev.mikchan.mcnp.votereceiver.config.IConfig

internal class FallbackConfig : IConfig {
    override fun reload(): Boolean {
        return true
    }

    override val port: Int get() = 6418
    override val mineServTopKey: String? get() = null
    override val testEnabled: Boolean get() = false
}
