package ru.cli.commands

import ru.cli.Environment
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `echo` unix command functionality.
 * `echo` is a command that outputs the strings that are passed to it as arguments
 */
class EchoCommand(override val args: List<String>) : Command {
    /**
     * Executes `echo` command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     * @param environment the environment of process
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream, environment: Environment): ReturnCode {
        out.write((args.joinToString(" ") + System.lineSeparator()).toByteArray())
        return ReturnCode(StatusCode.SUCCESS, 0)
    }
}
