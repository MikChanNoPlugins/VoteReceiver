package dev.mikchan.mcnp.votereceiver.utility.base

import dev.mikchan.mcnp.votereceiver.utility.IUtility
import java.security.MessageDigest

internal class Utility : IUtility {
    override fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.fold("") { str, b -> str + "%02x".format(b) }
    }
}
