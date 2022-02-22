package ru.cli.commands

import ru.cli.Environment
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.lang.System.lineSeparator

/**
 * This class provides `ls` bash command functionality.
 * `ls` is a command that outputs files in directory
 */
class LsCommand(override val args: List<String>) : Command {
    /**
     * Executes `ls` command
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
            out.printDir(environment.currentPath)
            ReturnCode(StatusCode.SUCCESS, 0)
        }
        1 -> {
            val file = environment.currentPath.resolve(args[0])
            if (!file.exists() || !file.canRead()) {
                error.write("${args[0]} no such file or directory${lineSeparator()}".toByteArray())
                ReturnCode(StatusCode.ERROR, 1)
            } else if (file.isDirectory) {
                out.printDir(file)
                ReturnCode(StatusCode.SUCCESS, 0)
            } else {
                out.write((args[0] + lineSeparator()).toByteArray())
                ReturnCode(StatusCode.SUCCESS, 0)
            }
        }
        else -> {
            error.write("Wrong number of args for ls command: expected 0 or 1${lineSeparator()}".toByteArray())
            ReturnCode(StatusCode.ERROR, 1)
        }
    }

    private fun OutputStream.printDir(dir: File) {
        dir.listFiles()?.forEach {
            write((it.name + lineSeparator()).toByteArray())
        }
    }
}
