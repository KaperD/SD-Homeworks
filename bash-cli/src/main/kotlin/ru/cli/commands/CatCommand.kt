package ru.cli.commands

import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `cat` unix command functionality.
 * `cat` is a standard Unix utility that reads files sequentially, writing them to standard output
 */
class CatCommand(private val args: List<String>) : Command {
    /**
     * Executes `cat` command
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

        val filename = args[0]
        val file = File(filename)
        return if (file.exists()) {
            out.write(file.readBytes())
            ReturnCode.SUCCESS
        } else {
            error.write(("$filename: No such file or directory").toByteArray())
            ReturnCode.ERROR
        }
    }
}