package ru.cli.commands

import ru.cli.Environment.vars
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides assignment to the environment variable
 */
class AssignmentCommand(private val args: List<String>) : Command {
    /**
     * Executes assignment to the environment variable
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream): ReturnCode {
        if (args.size != 2) {
            error.write(("Wrong number of arguments").toByteArray())
            return ReturnCode.ERROR
        }

        vars[args[0]] = args[1]
        return ReturnCode.SUCCESS
    }
}
