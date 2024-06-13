package dev.mikchan.mcnp.votereceiver.core.utility

/**
 * Utility
 *
 * A collection of useful functions
 */
interface IUtility {

    /**
     * SHA-256 hashing algorithm
     *
     * Computes the SHA-256 hash of the input string
     *
     * Behaves the same way `hash('sha256', $input)` does in PHP
     *
     * @return SHA-256 hash string
     */
    fun sha256(input: String): String

    /**
     * Md5 hash algorithm
     *
     * @return Md5 hash string
     */
    fun md5(input: String): String

    /**
     * SHA-1 hashing algorithm
     *
     * @return SHA-1 hash string
     */
    fun sha1(input: String): String
}
