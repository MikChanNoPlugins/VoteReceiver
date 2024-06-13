package dev.mikchan.mcnp.votereceiver.core.utility.base

import dev.mikchan.mcnp.votereceiver.core.utility.IUtility
import java.security.MessageDigest

internal class Utility : IUtility {
    private fun hash(input: String, alg: String): String {
        val md = MessageDigest.getInstance(alg)
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.fold("") { str, b -> str + "%02x".format(b) }
    }

    override fun sha256(input: String): String = hash(input, "SHA-256")
    override fun md5(input: String): String = hash(input, "MD5")
}
