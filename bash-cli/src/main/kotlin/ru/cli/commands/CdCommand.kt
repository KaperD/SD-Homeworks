package ru.cli.commands

import ru.cli.Environment
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.lang.System.lineSeparator

class CdCommand(override val args: List<String>) : Command {
    private val homeDir = File(System.getProperty("user.home")).also { assert(it.isDirectory) }

    override fun execute(
        input: InputStream,
        out: OutputStream,
        error: OutputStream,
        environment: Environment
    ): ReturnCode = when (args.size) {
        0 -> {
            environment.currentPath = homeDir
            ReturnCode(StatusCode.SUCCESS, 0)
        }
        1 -> {
            val newPath = environment.currentPath.resolve(args[0]).canonicalFile
            if (newPath.isDirectory) {
                environment.currentPath = newPath
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
