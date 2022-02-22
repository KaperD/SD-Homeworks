package ru.cli

import java.io.File

/**
 * This class stores our environmental variables in hash map
 * and our current working directory path
 */
data class Environment(
    val vars: MutableMap<String, String> = HashMap(),
    var currentPath: File = File("").absoluteFile
) {
    init {
        assert(currentPath.isDirectory)
    }
}
