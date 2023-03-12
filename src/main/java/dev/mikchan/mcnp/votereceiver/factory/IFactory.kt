package dev.mikchan.mcnp.votereceiver.factory

import com.vexsoftware.votifier.NuVotifierBukkit
import dev.mikchan.mcnp.votereceiver.config.IConfig
import dev.mikchan.mcnp.votereceiver.utility.IUtility
import io.ktor.server.engine.*

interface IFactory {
    fun createConfig(): IConfig
    fun createUtility(): IUtility
    fun createApplicationEngine(): ApplicationEngine
    fun createVoteHandler(): NuVotifierBukkit?
}
