package ru.cli.commands

import ru.cli.Environment
import java.io.InputStream
import java.io.OutputStream

/**
 * This class provides `pwd` unix command functionality.
 * `pwd`(print working directory) is a utility that gives the full pathname of the current working directory
 */
class PwdCommand(override val args: List<String>) : Command {
    /**
     * Executes `pwd` command
     *
     * @param input the input stream
     * @param out the output stream
     * @param error the error stream
     * @param environment the environment of process
     *
     * @return the execution code
     */
    override fun execute(input: InputStream, out: OutputStream, error: OutputStream, environment: Environment): ReturnCode {
        out.write("${environment.currentPath.path}${System.lineSeparator()}".toByteArray())
        return ReturnCode(StatusCode.SUCCESS, 0)
    }
}
