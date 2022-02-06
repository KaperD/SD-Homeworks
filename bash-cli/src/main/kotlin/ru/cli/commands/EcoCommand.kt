package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `echo` unix command functionality.
 * `echo` is a command that outputs the strings that are passed to it as arguments
 */
class EcoCommand(private val args: List<String>) : Command {
    /**
     * Executes `echo` command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream): ReturnCode {
        if (args.isEmpty()) {
            error.write(("Not enough arguments").toByteArray())
            return ReturnCode.ERROR
        }

        val builder = StringBuilder()
        for (arg in args) {
            builder.append(arg)
        }
        out.write(builder.toString().toByteArray())
        return ReturnCode.SUCCESS
    }
}