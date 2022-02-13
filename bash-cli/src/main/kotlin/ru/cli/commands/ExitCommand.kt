package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides exit from interpreter
 */
class ExitCommand(override val args: List<String>) : Command {
    /**
     * Exits from interpreter
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream): ReturnCode {
        return ReturnCode(StatusCode.EXIT, 0)
    }
}
