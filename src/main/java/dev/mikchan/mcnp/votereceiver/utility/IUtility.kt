package dev.mikchan.mcnp.votereceiver.utility

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
}
