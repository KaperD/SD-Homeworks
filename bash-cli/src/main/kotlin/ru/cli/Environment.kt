package ru.cli

import java.io.File

/**
 * This class stores our environmental variables in hash map
 * and our current working directory path
 */
class Environment(
    val vars: MutableMap<String, String> = HashMap(),
    workingDir: File = File("").absoluteFile
) {
    var workingDir: File = workingDir
        set(value) {
            assert(value.isDirectory)
            field = value
        }

    init {
        assert(workingDir.isDirectory)
    }
}
