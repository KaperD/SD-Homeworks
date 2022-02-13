package ru.cli

/**
 * This class stores our environmental variables in hash map
 */
data class Environment(val vars: MutableMap<String, String> = HashMap())
