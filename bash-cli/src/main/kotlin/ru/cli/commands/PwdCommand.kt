package ru.cli.commands

import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `pwd` unix command functionality.
 * `pwd`(print working directory) is a utility that gives the full pathname of the current working directory
 */
class PwdCommand(private val args: List<String>) : Command {
    /**
     * Executes `pwd` command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream): ReturnCode {
        out.write(getCurrentDirectory().toByteArray())
        return ReturnCode.SUCCESS
    }

    private fun getCurrentDirectory(): String {
        return System.getProperty("user.dir") + '\n'
    }
}
