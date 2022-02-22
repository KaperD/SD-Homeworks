package ru.cli.commands

import ru.cli.Environment
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.lang.System.lineSeparator

/**
 * This class provides `cd` bash command functionality.
 * `cd` is a command that changes shell working directory
 */
class CdCommand(override val args: List<String>) : Command {
    private val homeDir = File(System.getProperty("user.home")).also { assert(it.isDirectory) }

    /**
     * Executes `cd` command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     * @param environment the environment of process
     *
     * @return the execution code
     */
    override fun execute(
        input: InputStream,
        out: OutputStream,
        error: OutputStream,
        environment: Environment
    ): ReturnCode = when (args.size) {
        0 -> {
            environment.workingDir = homeDir
            ReturnCode(StatusCode.SUCCESS, 0)
        }
        1 -> {
            val newPath = environment.workingDir.resolve(args[0]).canonicalFile
            if (newPath.isDirectory) {
                environment.workingDir = newPath
                ReturnCode(StatusCode.SUCCESS, 0)
            } else if (newPath.exists()) {
                error.write("${args[0]} is not a directory${lineSeparator()}".toByteArray())
                ReturnCode(StatusCode.ERROR, 1)
            } else {
                error.write("${args[0]} no such file or directory${lineSeparator()}".toByteArray())
                ReturnCode(StatusCode.ERROR, 1)
            }
        }
        else -> {
            error.write("Wrong number of args for cd command: expected 0 or 1${lineSeparator()}".toByteArray())
            ReturnCode(StatusCode.ERROR, 1)
        }
    }
}
