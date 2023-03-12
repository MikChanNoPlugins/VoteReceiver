package dev.mikchan.mcnp.votereceiver.config

interface IConfig {
    fun reload(): Boolean

    val port: Int
    val mineServTopKey: String?
}
