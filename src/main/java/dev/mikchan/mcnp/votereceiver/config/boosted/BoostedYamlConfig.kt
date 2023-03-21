package dev.mikchan.mcnp.votereceiver.config.boosted

import dev.dejvokep.boostedyaml.YamlDocument
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings
import dev.mikchan.mcnp.votereceiver.config.IConfig
import java.io.File
import java.io.InputStream

internal class BoostedYamlConfig(document: File, resource: InputStream) : IConfig {
    private val config: YamlDocument = YamlDocument.create(
        document,
        resource,
        GeneralSettings.DEFAULT,
        LoaderSettings.builder().setAutoUpdate(true).build(),
        DumperSettings.DEFAULT,
        UpdaterSettings.builder().setVersioning(BasicVersioning("config-version")).build()
    )

    override fun reload(): Boolean {
        return config.reload()
    }

    override val port: Int get() = config.getInt("port", 6418)
    override val mineServTopKey: String? get() = config.getString("mineserv-top-key")
    override val testEnabled: Boolean get() = config.getBoolean("enable-test-route", false)
}
